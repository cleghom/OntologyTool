package ioOperation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import staticVariable.staticvalue;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.OWLModelWriter;

public class WriteToFile {
    /*
     * 写入 操作
     */
    public static void writetoFile(OWLModel o) throws IOException {
        // 如果文件夹不存在则创建
        String localaddr = staticvalue.localaddr;
        File file = new File(localaddr);
        if (!file.exists() && !file.isDirectory()) {
            System.out.println("//不存在");
            file.mkdirs();
        } else {
            System.out.println("//目录存在");
        }
        String time = staticvalue.tempfilename;
        String filePathOut01 = localaddr + "/" + time + ".owl";
        // 写入：
        FileOutputStream outFile = new FileOutputStream(filePathOut01);
        Writer out = new OutputStreamWriter(outFile, "UTF-8");
        OWLModelWriter omw = new OWLModelWriter(o, o.getTripleStoreModel().getActiveTripleStore(), out);
        omw.write();
        out.close();
    }

    /*
     * 写入 操作
     */
    public static void writetoFile(OWLModel o, String uri) throws IOException {
        // 如果文件夹不存在则创建
        String localaddr = staticvalue.localaddr;
        File file = new File(localaddr);
        if (!file.exists() && !file.isDirectory()) {
            System.out.println("//不存在");
            file.mkdirs();
        } else {
            System.out.println("//目录存在");
        }
        // 写入：
        FileOutputStream outFile = new FileOutputStream(uri);
        Writer out = new OutputStreamWriter(outFile, "UTF-8");
        OWLModelWriter omw = new OWLModelWriter(o, o.getTripleStoreModel().getActiveTripleStore(), out);
        omw.write();
        out.close();
    }
}
