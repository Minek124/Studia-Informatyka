import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.FileChannelWrapper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.LinkedBlockingQueue;

class MovieToBufferedImage implements MovieToBufferedImageInterface {

    FrameGrab grab = null;
    int size = -1;
    Thread slave;
    LinkedBlockingQueue<BufferedImage> queue = new LinkedBlockingQueue<BufferedImage>();

    class Worker implements Runnable {

        MovieToBufferedImage master;

        public Worker(MovieToBufferedImage master) {
            this.master = master;
        }

        @Override
        public void run() {
            while(true) {
                if(master.queue.size() < master.size) {
                    try {
                        System.out.println(master.queue.size() + 1 + " / " + master.size);
                        queue.add(master.grab.getFrame());
                    } catch (NullPointerException e1){
                        master.size = -1;
                        break;
                    }catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }
    }

    public MovieToBufferedImage(){
        slave = new Thread(new Worker(this));
        slave.start();
    };

    public void setFile(File movie){
        try {
            grab = new FrameGrab(new FileChannelWrapper(new
                    RandomAccessFile(movie, "r").getChannel()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JCodecException e) {
            e.printStackTrace();
        }
    }

    public void setQueueMaxSize(int size) {
        this.size = size;
    }

    public BufferedImage getFrame() {
        while(true) {
            if (!queue.isEmpty()) {
                return queue.poll();
            }else if (size == -1) {
                return null;
            }
        }
    }
}
