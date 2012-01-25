<%@ page
  contentType="text/html; charset=UTF-8"
  import="javax.servlet.*"
  import="javax.servlet.http.*"
  import="java.io.*"
  import="java.util.*"
  import="org.apache.hadoop.fs.*"
  import="org.apache.hadoop.hdfs.*"
  import="org.apache.hadoop.hdfs.server.namenode.*"
  import="org.apache.hadoop.hdfs.server.datanode.*"
  import="org.apache.hadoop.hdfs.server.common.Storage"
  import="org.apache.hadoop.hdfs.server.common.Storage.StorageDirectory"
  import="org.apache.hadoop.hdfs.protocol.*"
  import="org.apache.hadoop.util.*"
  import="java.text.DateFormat"
  import="java.lang.Math"
  import="java.net.URLEncoder"
%>

<%
  FSImage fsImage = (FSImage)application.getAttribute("name.system.image");

           StorageDirectory st =null;
          for (Iterator<StorageDirectory> it = fsImage.dirIterator(); it.hasNext();) {
              st = it.next();
              String dir = "" +  st.getRoot();
                  String type = "" + st.getStorageDirType();
              Field field = fsImage.getClass().getDeclaredField("removedStorageDirs");
              field.setAccessible(true);

	      //List dirs = (List)(field.get(fsImage));
              //dirs.size(); 
              //fsImage.removedStorageDirs.add(st);
              //it.remove();
          }
 
%>
