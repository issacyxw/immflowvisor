package org.flowvisor.classifier;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONStyle;
import net.minidev.json.parser.ParseException;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
import org.flowvisor.slicer.FVSlicer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class server {
        public void startThread(FVClassifier fvc) throws IOException, ParseException {
        //        Server_Test s = new Server_Test(licerMap,weightMap);
            Server_Test s = new Server_Test(fvc);
            s.start();
        }
    
        class Server_Test extends Thread {
            public FVClassifier fvclassifier;
            public Server_Test(FVClassifier fvc){
                fvclassifier=fvc;
    
            }

            @Override
            public void run() {
                super.run();
    
                String str_send = "Hello UDPclient";
                byte[] buf = new byte[1024];
                //服务端在3000端口监听接收到的数据
                DatagramSocket ds = null;
                try {
                    ds = new DatagramSocket(9999);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                //接收从客户端发送过来的数据
                DatagramPacket dp_receive = new DatagramPacket(buf, 1024);
                System.out.println("server is on，waiting for client to send data......");
                boolean f = true;
                System.out.println("debug1");
                while (f) {
                    //服务器端接收来自客户端的数据
                    try {
                        ds.receive(dp_receive);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("server received data from client：");
                    String str_receive = new String(dp_receive.getData(), 0, dp_receive.getLength());
    //            String str_receive = new String(dp_receive.getData(),0,dp_receive.getLength()) +
    //                    " from " + dp_receive.getAddress().getHostAddress() + ":" + dp_receive.getPort();
                    System.out.println("str_receive="+str_receive);
                    Object obj = null;
                    try {
                        obj = JSONValue.parseStrict(str_receive);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    JSONObject obj3 = (JSONObject) obj;
                    System.out.println("begin");
                    // fvclassifier.weightMap.clear();
//                    System.out.println("fv.slicerMap="+fvclassifier.slicerMap+"    begin setting, fv.weightMap="+fvclassifier.weightMap);
                    // for (String fvname : fvclassifier.slicerMap.keySet()){
                    for (FVSlicer fff : fvclassifier.getSlicers()) {
                        String fvname=fff.getName();
                        System.out.println(fvname);
                        // fvclassifier.weightMap.put(fvname,(int) obj3.get(fvname));
                        fvclassifier.weightMap.put(fvname,Integer.parseInt(obj3.get(fvname).toString()));
//                        System.out.println("After setting, fv.weightMap="+fvclassifier.weightMap.get(fvname));
    
                    }

                    dp_receive.setLength(1024);
                }
                System.out.println("debug2");
                ds.close();
            }
        }
    }