
/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.*;
import java.net.*;

public class EchoClient implements Runnable {
	public static int portnum = 0;
	public Thread runner;

	public static boolean connected = false;

	public EchoClient(String client) {
		runner = new Thread(this, client);
		runner.start();
	}

	public void run() {

		try (ServerSocket serverSocket = new ServerSocket(portnum);
				Socket clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		) {
			System.out.println("test");
			String inputLine;
			connected = true;
			  while ((inputLine = in.readLine()) != null) {
	                out.println(inputLine);
	                System.out.println(inputLine);
	           }

		} catch (IOException e) {

		}
	}

	public static void main(String[] args) throws IOException {

		if (args.length != 2) {
			System.err.println("Usage: java EchoClient <host name> <port number>");
			System.exit(1);
		}
		String pHostName;
		int pPortNum;
		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);
		try (Socket echoSocket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

			String userInput;

			while ((userInput = stdIn.readLine()) != null) {
				
				if (!connected) {

					out.println(userInput);

					// check if client is asking for list of users if yes then
					// list
					// otherwise echo what they typed

					// Thread test = new Thread(new EchoClient("message"));
					if(userInput.equals("CONNECT")){
						System.out.println("peers to start chat with:");
						String listOfPlayers = in.readLine();
						listOfPlayers = listOfPlayers.substring(1, listOfPlayers.length() - 1);
						listOfPlayers = listOfPlayers.replaceAll(", ", "\n");
						listOfPlayers = listOfPlayers.replaceAll("=", " has an open port on ");
						System.out.println(listOfPlayers);
						System.out.println();
						System.out.print("host name:");
						pHostName = stdIn.readLine();
						System.out.print("Port num:");
						pPortNum = Integer.parseInt(stdIn.readLine());
						try (Socket echoSocket2 = new Socket(pHostName, pPortNum);
								PrintWriter out2 = new PrintWriter(echoSocket2.getOutputStream(), true);
								BufferedReader in2 = new BufferedReader(new InputStreamReader(echoSocket2.getInputStream()));
								BufferedReader stdIn2 = new BufferedReader(new InputStreamReader(System.in))) {
							 String userInput2;
							 System.out.println("test2");
					            while ((userInput2 = stdIn2.readLine()) != null) {
					                out.println(userInput2);
					                System.out.println("echo: " + in2.readLine());
					            }
							
							
						}catch (UnknownHostException e) {
							System.err.println("Don't know about host " + hostName);
							System.exit(1);
						} catch (IOException e) {
							System.err.println("Couldn't get I/O for the connection to " + hostName);
							System.exit(1);
						}
						
					}
					if (userInput.equals("LIST")) {
						// server returns an array converted to an array.
						// removed the [] and change the commas to new lines
						String listOfPlayers = in.readLine();
						listOfPlayers = listOfPlayers.substring(1, listOfPlayers.length() - 1);
						listOfPlayers = listOfPlayers.replaceAll(", ", "\n");
						listOfPlayers = listOfPlayers.replaceAll("=", " has an open port on ");
						System.out.println(listOfPlayers);
					} else {
						String servout = in.readLine();
						if (servout.length() > 7 && servout.substring(0, 7).equals("Welcome")) {
							portnum = Integer.parseInt(userInput);
							Thread test = new Thread(new EchoClient("message"));
						}
						System.out.println(servout);
					}
				}
			}
			echoSocket.close();
		}catch(

	UnknownHostException e)
	{
		System.err.println("Don't know about host " + hostName);
		System.exit(1);
	}catch(
	IOException e)
	{
		System.err.println("Couldn't get I/O for the connection to " + hostName);
		System.exit(1);
	}
}}
