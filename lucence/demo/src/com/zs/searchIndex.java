package com.zs;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class searchIndex {
    private IndexSearcher indexSearcher;
    private IndexReader indexReader;
    @Before
    public void init() throws IOException {
        indexReader= DirectoryReader.open(FSDirectory.open(new File("D:\\ssm\\lucene\\index").toPath()));
        indexSearcher=new IndexSearcher(indexReader);
    }
    @Test
    public void testRangQuery() throws Exception {
        Query query = LongPoint.newRangeQuery("size", 0L, 100L);
        printResult(query);
        
    }
    private void printResult(Query query) throws Exception {
        //执行查询
        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("总记录数：" + topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc doc:scoreDocs){
            //取文档id
            int docId = doc.doc;
            Document doc1 = indexSearcher.doc(docId);
            System.out.println("名字"+doc1.get("name"));
            System.out.println("路径"+doc1.get("path"));
            System.out.println("大小"+doc1.get("size"));
            System.out.println("内容"+doc1.get("content"));
            System.out.println("-----------------寂寞的分割线");
        }
        indexReader.close();
    }
    @Test
    public void testQueryParser() throws Exception {
        //创建一个QueryPaser对象，两个参数
        QueryParser queryParser = new QueryParser("name", new IKAnalyzer());
        //参数1：默认搜索域，参数2：分析器对象
        //使用QueryPaser对象创建一个Query对象
        Query query = queryParser.parse("lucene是一个Java开发的全文检索工具包");
        //执行查询
        printResult(query);
    }

}
