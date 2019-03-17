package com.zs;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.awt.*;
import java.io.File;

import java.io.IOException;





public class demo {
    @Test
    public void demoTest() throws IOException {
        //读取原始文档
        File file = new File("D:\\ssm\\lucene\\02.参考资料\\searchsource");
        //创建document对象建立索引库
        Directory directory = FSDirectory.open(new File("D:\\ssm\\lucene\\index").toPath());
        //基于Directory对象创建一个IndexWriter对象
        IndexWriterConfig config=new IndexWriterConfig(new IKAnalyzer());
        IndexWriter writer=new IndexWriter(directory,config);
        File[] files = file.listFiles();
        //2、基于Directory对象创建一个IndexWriter对象
        for (File file1 : files) {
            String file1Name = file1.getName();
            String path = file1.getPath();
            String content = FileUtils.readFileToString(file1, "UTF-8");
            long size = FileUtils.sizeOf(file1);
            //创建Field
            //参数1：域的名称，参数2：域的内容，参数3：是否存储

            Field fieldName = new TextField("name",file1Name, Field.Store.YES);
            Field path1 = new StoredField("path",path);
            Field content1 = new TextField("content",content, Field.Store.YES);
          //  Field size1= new LongPoint("size",size);
           //Field fieldSizeStore = new StoredField("size", size);
           // Field size1 = new TextField("size",size, Field.Store.YES);
          Field size1= new TextField("size",size+"", Store.YES);
            Document document=new Document();
            //向文档对象中添加域
            document.add(fieldName);
            document.add(path1);
            document.add(content1);
            document.add(size1);
            //document.add(fieldSizeStore);
            //5、把文档对象写入索引库

            writer.addDocument(document);
            //6、关闭indexwriter对象

        }
        writer.close();

    }
    @Test

    public void indexSerchaer() throws IOException {
        //第一步：创建一个Directory对象，也就是索引库存放的位置。
        Directory directory = FSDirectory.open(new File("D:\\ssm\\lucene\\index").toPath());
        //第二步：创建一个indexReader对象，需要指定Directory对象。
        IndexReader indexReader = DirectoryReader.open(directory);
        //第三步：创建一个indexsearcher对象，需要指定IndexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //第四步：创建一个TermQuery对象，指定查询的域和查询的关键词。
        Query query =new TermQuery(new Term("name","apache"));
        //第五步：执行查询。
        TopDocs topDocs = indexSearcher.search(query, 10);
        //第六步：返回查询结果。遍历查询结果并输出。
        System.out.println("查询总记录数"+topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int doc = scoreDoc.doc;
            Document doc1 = indexSearcher.doc(doc);
            System.out.println("名字"+doc1.get("name"));
            System.out.println("路径"+doc1.get("path"));
            System.out.println("大小"+doc1.get("size"));
            System.out.println("内容"+doc1.get("content"));
            System.out.println("============================");
        }
        //第七步：关闭IndexReader对象
        indexReader.close();

    }

    @Test
    public void testTokenStream() throws IOException {
        //1）创建一个Analyzer对象，IKAnalyzer对象
        Analyzer analyzer = new IKAnalyzer();
        //2）使用分析器对象的tokenStream方法获得一个TokenStream对象
        TokenStream tokenStream =analyzer.tokenStream("","2017年12月14日 - 传智播客Lucene概述公安局Lucene是一款高性能的、可扩展的信息检索(IR)工具库。信息检索是指文档搜索、文档内信息搜索或者文档相关的元数据搜索等操作。");
        //3）向TokenStream对象中设置一个引用，相当于数一个指针
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        //4）调用TokenStream对象的rest方法。如果不调用抛异常
        tokenStream.reset();
        //5）使用while循环遍历TokenStream对象
        while (tokenStream.incrementToken()){
            System.out.println(charTermAttribute.toString());
        }
        //6）关闭TokenStream对象
        tokenStream.close();
    }
}
