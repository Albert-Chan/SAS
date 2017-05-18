package data.crawler;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class SinajsCrawler {

     public void crawle(String... stockIds) {
          StringBuffer sb = new StringBuffer();
          // "http://hq.sinajs.cn/list=sh600151,sz000830,s_sh000001,s_sz399001,s_sz399106,sz000777";
          sb.append("http://hq.sinajs.cn/list=");
          boolean firstOne = true;
          for (String stockId : stockIds) {
               if (firstOne) {
                    sb.append(stockId);
                    firstOne = false;
               } else {
                    sb.append(",").append(stockId);
               }
          }

          String url = sb.toString();

          HashMap<String, Float> high = new HashMap<String, Float>();

          try {
               URL u = new URL(url);
               byte[] bytes = new byte[256];
               InputStream in = null;
               ByteArrayOutputStream out = new ByteArrayOutputStream();
               while (true) {
                    try {
                         in = u.openStream();
                         int size;
                         while ((size = in.read(bytes)) != -1) {
                              out.write(bytes, 0, size);
                         }
                         String result = out.toString("gb2312");
                         String[] stocks = result.split(";\n");
                         for (String stock : stocks) {
                              String[] datas = stock.split(",");
                              String[] stockInfo = getStockInfo(datas[0]);
                              String stockId = stockInfo[0];
                              String stockName = stockInfo[1];

                              float highest = 0f;
                              if (high.containsKey(stockId)) {
                                   highest = high.get(stockId);
                              }

                              float currentPrice = Float.parseFloat(datas[1]);

                              // the next line is for test.
                              // alert("卖出 [" + stockId +"] " + stockName + "! \r\n" +
                              // "当前价格：" + currentPrice + "\r\n" + "从最高价" +highest
                              // +"跌落");

                              if (currentPrice < highest) {
                                   if (((highest - currentPrice) / highest) > 0.018) {
                                        alert("卖出 [" + stockId + "] " + stockName
                                                  + "! \r\n" + "当前价格：" + currentPrice
                                                  + "\r\n" + "从最高价" + highest + "跌落");
                                   }
                              }

                              if (currentPrice > highest) {
                                   high.put(stockId, currentPrice);
                              }

                         }
                         out.reset();
                    } catch (Exception e) {
                         System.out.println(e.getMessage());
                    } finally {
                         if (in != null) {
                              in.close();
                         }
                    }
               }
          } catch (Exception ex) {
               System.out.println(ex.getMessage());
          }

     }

     private String[] getStockInfo(String data) {
          String[] strs = data.split("=\"");
          if (strs != null && strs.length == 2) {
               int index = strs[0].lastIndexOf("_");
               String stockId = strs[0].substring(index + 1);
               String stockName = strs[1];
               return new String[] { stockId, stockName };
          }
          return null;
     }

     private void alert(String msg) {
          JDialog dialog = new JDialog();
          dialog.setAlwaysOnTop(true);
          JOptionPane.showMessageDialog(dialog, msg, "Sale Out!",
                    JOptionPane.WARNING_MESSAGE);
     }

     public static void main(String[] args) {
          SinajsCrawler crawler = new SinajsCrawler();
          crawler.crawle(args);
     }
}