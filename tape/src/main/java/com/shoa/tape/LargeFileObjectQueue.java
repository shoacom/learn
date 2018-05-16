// Copyright 2012 Square, Inc.
package com.shoa.tape;

import com.squareup.tape.FileException;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.QueueFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Base queue class, implements common functionality for a QueueFile-backed
 * queue manager.  This class is not thread safe; instances should be kept
 * thread-confined.
 * <p>
 * The {@link #add(Object)}, {@link #peek()}, {@link #remove()}, and
 * {@link #setListener(Listener)} methods may throw a
 * {@link FileException} if the underlying {@link QueueFile} experiences an
 * {@link IOException}.
 *
 * @param <T> The type of elements in the queue.
 */
public class LargeFileObjectQueue<T> implements ObjectQueue<T> {
  /**
   * Backing storage implementation.
   */
  private final LargeQueueFile queueFile;
  /**
   * Reusable byte output buffer.
   */
  private final DirectByteArrayOutputStream bytes = new DirectByteArrayOutputStream();
  /**
   * Keep file around for error reporting.
   */
  private final File file;
  private final FileObjectQueue.Converter<T> converter;
  private Listener<T> listener;

  public LargeFileObjectQueue(File file, FileObjectQueue.Converter<T> converter) throws IOException {
    this.file = file;
    this.converter = converter;
    this.queueFile = new LargeQueueFile(file);
  }

  @Override
  public int size() {
    return queueFile.size();
  }

  @Override
  public final void add(T entry) {
    try {
      bytes.reset();
      converter.toStream(entry, bytes);
      queueFile.add(bytes.getArray(), 0, bytes.size());
      if (listener != null) listener.onAdd(this, entry);
    } catch (IOException e) {
      throw new FileException("Failed to add entry.", e, file);
    }
  }

  @Override
  public T peek() {
    try {
      byte[] bytes = queueFile.peek();
      if (bytes == null) return null;
      return converter.from(bytes);
    } catch (IOException e) {
      throw new FileException("Failed to get.", e, file);
    }
  }

  @Override
  public final void remove() {
    try {
      queueFile.remove();
      if (listener != null) listener.onRemove(this);
    } catch (IOException e) {
      throw new FileException("Failed to remove.", e, file);
    }
  }

  public final void close() {
    try {
      queueFile.close();
    } catch (IOException e) {
      throw new FileException("Failed to close.", e, file);
    }
  }

  @Override
  public void setListener(final Listener<T> listener) {
    if (listener != null) {
      try {
        queueFile.forEach((in, length) -> {
          byte[] data = new byte[length];
          in.read(data, 0, length);

          listener.onAdd(LargeFileObjectQueue.this, converter.from(data));
        });
      } catch (IOException e) {
        throw new FileException("Unable to iterate over QueueFile contents.", e, file);
      }
    }
    this.listener = listener;
  }


  /**
   * Enables direct access to the internal array. Avoids unnecessary copying.
   */
  private static class DirectByteArrayOutputStream extends ByteArrayOutputStream {
    public DirectByteArrayOutputStream() {
      super();
    }

    /**
     * Gets a reference to the internal byte array.  The {@link #size()} method indicates how many
     * bytes contain actual data added since the last {@link #reset()} call.
     */
    public byte[] getArray() {
      return buf;
    }
  }
}
