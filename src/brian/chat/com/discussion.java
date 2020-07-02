package javaswingclass;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import javax.swing.Timer;
import javax.swing.*;



public class discussion extends JFrame implements ActionListener {
	  JTextArea t2,texts,texts2;
	  JLabel t1,label;
	  JButton b1,send;
	  
	   
	  private static final String TERMINATE = "Exit";
    static String name;
    static volatile boolean finished = false;
   
	public void screen() {
		
	}
	  discussion(){
		  setTitle("Simple addition and substraction calculator"); 
		  
		  t1 = new JLabel("Enter username");
		  t2 = new JTextArea();
		  
	         
		  
		  b1 = new JButton("Ok");
		
		  
		  t1.setBounds(50,50,200,50);
		  t2.setBounds(50,100,150,20);
	
	         
		  
		  
		 
	         texts = new JTextArea("me: ");
	         texts2 = new JTextArea("Texts from friend");
	         label = new JLabel("type text");
	         send = new JButton("Send");
	        
	        label.setBounds(50,10,100,30);
	  	    texts.setBounds(140,380,500,200);
	  	  texts2.setBounds(140,150,500,200);
	  	    send.setBounds(400,680,200,50);
	  	  b1.setBounds(200,680,100,50);
	  	  
	  	    add(label);
	  	    add(texts);
	  	  add(texts2);
	  	    add(send);
	  	    
	      
		  add(t1);
		  add(t2);
		  
		  
		  add(b1);
		  b1.addActionListener(this);
		  
		  
		  setSize(800,780);
		  setResizable(true);
		  
		  
		  
	        setLayout(null);
	        setVisible(true);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		if(s != "") {
//			  b1.addActionListener(this);
//				b1.addActionListener(e -> {
//					frame.dispose();
//				});
//		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//		}
			

			
		   
	  }
	  public void FileOutputStream(String name,boolean append) {
		  //leave to empty
	}
		public void dowork() {
			 String s2 = t2.getText();
			try {
                InetAddress group = InetAddress.getByName("227.0.0.1");
                int port = Integer.parseInt("4020");
//                Scanner sc = new Scanner(System.in);
//                System.out.print("Enter your name: ");
                name = s2;
                MulticastSocket socket = new MulticastSocket(port);
                socket.setTimeToLive(0);
                socket.joinGroup(group);
                
                Thread t = new Thread(new ReadThread(socket, group, port));
                t.start();
              
           
                System.out.println("Start typing messages...\n"); 
               
               
      
           
            	
          
                while (true) {
                   
                      send.addActionListener(new ActionListener() {

					
						public void actionPerformed(ActionEvent arg0) {
						SwingWorker <Void, Void>wk =new SwingWorker<Void,Void>(){
							public Void doInBackground() throws Exception{
								Scanner sc = new Scanner(System.in);
					               String message =  texts.getText();
									 message = name + ": " + message;
				                       byte[] buffer = message.getBytes();
				                       DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
				                       try {
										socket.send(datagram);
										
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								return null;
								
							}
						};
						send.setEnabled(false);
						wk.execute();
						send.setEnabled(true);
					
						}
                    	  
                      }); 
                       
               }
              
               
                	
    			
          } 
          catch (SocketException se) {
                System.out.println("Error creating socket");
                se.printStackTrace();
          } 
          catch (IOException ie) {
                System.out.println("Error reading/writing from/to socket");
                ie.printStackTrace();
          }
		}
		
	  
		  public void actionPerformed(ActionEvent e) {
			 
				
				
				//==============this updates the GUI using SwingWorker============
				
				if(e.getSource() == b1) {
					SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

						@Override
						protected Void doInBackground() throws Exception {
							// TODO Auto-generated method stub
							dowork();
							return null;
						}
						
					};
			b1.setEnabled(false);
			worker.execute();
			b1.setEnabled(true);
					
				}
				
		  }
	  
	 

	  public static void main(String[] args) throws IOException{
		  new discussion();   
		
	  }
	  class ReadThread implements Runnable {
		    private MulticastSocket socket;
		    private InetAddress group;
		    private int port;
		    private static final int MAX_LEN = 1000;
		    
		 
			  
			
		    ReadThread(MulticastSocket socket, InetAddress group, int port) {
		           this.socket = socket;
		           this.group = group;
		           this.port = port;
		    }
		    @Override
		  
		     public void run() {
		    	screen();
		    	String finmes;
		           while (!discussion.finished) {
		                   byte[] buffer = new byte[ReadThread.MAX_LEN]; 
		                  String bu= texts.getText();
		                  buffer = bu.getBytes();
		                   DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
		                  
		                   String message;
		                   
		        
		           try {
		        	
		                   socket.receive(datagram);
		                   message = new String(buffer, 0, datagram.getLength(), "UTF-8");
		               

		                   
		                   if (!message.startsWith(discussion.name))
		                         
		                	   	System.out.println(message);
		               	//===================write message to file input.txt================
	                       File file = new File("C:\\Users\\brian mwenda\\eclipse-workspace\\java course work\\input.txt");
	                  //open the file in append mode
	                       try (PrintStream out = new PrintStream(new FileOutputStream(file,true))){
	                    	  
	                    	   out.print(message + "\n");
	                    	  
	                       }
	                      
	                 //read content of the file and display to gui
	                       BufferedReader br = new BufferedReader(new FileReader(file));
	                       try {
	                    	   StringBuilder sb = new StringBuilder();
	                    	   String line = br.readLine();
	                    	   
	                    	   while(line != null) {
	                    		   sb.append(line + "\n");
	                    		   line = br.readLine();
	                    	   }
	                    	   String everything = sb.toString();
	                    	   texts2.setText(everything);
	                       }finally {
	                    	   br.close();
	                       }
	                      
		                        
		                  
                   			
		                   } 
		                   catch (IOException e) {
		                           System.out.println("Socket closed!");
		                   }
//		           finally {
//		        	   try {
//		        		   
//						Thread.sleep(500);
//						repaint();
//						
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//		           }
		            }
		     }
		}


}

