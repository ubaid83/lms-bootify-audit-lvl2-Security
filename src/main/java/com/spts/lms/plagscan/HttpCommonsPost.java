package com.spts.lms.plagscan;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
 
//download libraries here: 
//http://apache.mirror.clusters.cc/httpcomponents/commons-httpclient/binary/commons-httpclient-3.1.zip
//http://commons.apache.org/downloads/download_logging.cgi
//http://commons.apache.org/codec/download_codec.cgi
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.methods.multipart.*;

 
public class HttpCommonsPost
{
  private String url = null;
 
  public void setURL(final String url)
  {
    this.url = url;
  }
 
  public String doPostRequest(final Part[] caParts)
  {
    String lReturn = "";
    final HttpClient clHttpClient = new HttpClient();
    BufferedReader lBufferedReader = null;
    PostMethod lPostMethod = null;
 
    try
    {
      lPostMethod = new PostMethod(this.url);
   	  lPostMethod.setRequestEntity(
     		new MultipartRequestEntity(caParts, lPostMethod.getParams())
      		);
      lPostMethod.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);
      final int clReturnCode = clHttpClient.executeMethod(lPostMethod);
 
      if (clReturnCode != HttpStatus.SC_NOT_IMPLEMENTED)
      {
        lBufferedReader = new BufferedReader(new InputStreamReader(lPostMethod.getResponseBodyAsStream()));
        String lReadLine = null;
 
        while (((lReadLine = lBufferedReader.readLine()) != null))
        {
          lReturn = lReturn.concat(lReadLine + "\r\n");
        }
      }
      else
      {
        throw new Exception("Failed!");
      }
    }
    catch (final Exception caException)
    {
      System.err.println("Exception: " + caException.getMessage());
    }
    finally
    {
      lPostMethod.releaseConnection();
 
      if (lBufferedReader != null)
      {
        try
        {
          lBufferedReader.close();
        }
        catch (final Exception caException)
        {
          final Runtime clRuntime = Runtime.getRuntime();
          clRuntime.gc();
        }
      }
    }
 
    return lReturn;
  }
}