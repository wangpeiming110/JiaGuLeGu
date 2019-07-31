package com.zf.plugins.JiaGuLeGu

import java.nio.charset.Charset

class StreamConsumer extends Thread {
    InputStream is
    String type
    Closure closure
    String charset

    StreamConsumer(InputStream is, String type, Closure closure) {
        this(is, type, Charset.defaultCharset().name(), closure)
    }

    StreamConsumer(InputStream is, String type, String charset, Closure closure) {
        this.is = is
        this.type = type
        this.closure = closure
        this.charset = charset
    }


    public void run() {
        try {
            is.eachLine(charset) { line ->
                if (line?.trim()) {
                    if (closure) {
                        closure(type, line)
                    }
                }
            }

        } catch (Exception ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is) {
                    is.close()
                }
            } catch (Exception ex) {
                ex.printStackTrace()
            }
        }
    }
}
