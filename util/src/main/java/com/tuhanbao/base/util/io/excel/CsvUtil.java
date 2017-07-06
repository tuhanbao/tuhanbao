package com.tuhanbao.base.util.io.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;
import com.tuhanbao.base.util.io.IOUtil;
import com.tuhanbao.base.util.thread.ScheduledThreadManager;

public class CsvUtil {
    public static List<String> read(String url) throws IOException {
        return read(url, IOUtil.DEFAULT_CHARSET);
    }

    public static List<String> read(InputStream fi) throws IOException {
        return read(fi, IOUtil.DEFAULT_CHARSET);
    }
    
    public static List<String> read(InputStream fi, String charset) throws IOException {
        CsvReader reader = null;
        List<String> result = new ArrayList<String>();
        try
        {
            reader = new CsvReader(new InputStreamReader(fi, charset));
            //大记录时需要打开
            reader.setSafetySwitch(false);
            while (reader.readRecord()) {
                result.add(reader.getRawRecord());
            }
        }
        finally
        {
            IOUtil.close(fi);
            if (reader != null) reader.close();
        }

        return result;
    }
    
    public static StringPool readSync(InputStream is, String charset, int size) throws IOException {
        StringPool pool = new StringPool(size);
        ScheduledThreadManager.execute(new ReadCsvRunnable(is, pool, charset));
        return pool;
    }
    
    static class ReadCsvRunnable implements Runnable {
        private StringPool pool;
        
        private InputStream is;
        
        private String charset;
        
        public ReadCsvRunnable(InputStream is, StringPool pool, String charset) {
            this.is = is;
            this.pool = pool;
            this.charset = charset;
        }

        @Override
        public void run() {
            CsvReader reader = null;
            try {
                reader = new CsvReader(new InputStreamReader(is, charset));
                //大记录时需要打开
                reader.setSafetySwitch(false);
                while (reader.readRecord()) {
                    pool.add(reader.getRawRecord());
                }
            }
            catch (Throwable e) {
                pool.setError(e);
            }
            finally
            {
                pool.setFinish(true);
                IOUtil.close(is);
                if (reader != null) reader.close();
            }
        }
        
    }
    
    public static List<String> read(String url, String charset) throws IOException {
		return read(new FileInputStream(url), charset);
    }
}
