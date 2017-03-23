
/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
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

import java.net.*;
import java.io.*;
import java.util.ArrayList;

//implement Runnable to make threads
public class EchoServer implements Runnable {

	// create variables for threads socket and the list of users.
	public Thread runner;
	private static ArrayList<String> user = new ArrayList<String>();
	public static Socket clientSocket;

	// constructor class for EchoServer
	public EchoServer(String client) {
		runner = new Thread(this, client);
		runner.start();
	}

	// join method that adds clients to the server list when they do the join
	// command
	public synchronized boolean Join(String name) {
		if (user.contains(name)) {
			return true;
		} else {
			user.add(name);
			return false;
		}
	}

	// removes clients from list when they do the leave command
	public synchronized void Leave(String name) {
		user.remove(name);
	}

	// runable thread when a thread is created
	public void run() {
		// gets the Socket of the client for the thread
		Socket Tsocket = clientSocket;
		try (PrintWriter out = new PrintWriter(Tsocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(Tsocket.getInputStream()));) {
			// Boolean joined is to make sure they can only join once
			String inputLine;
			String name = null;
			boolean joined = true;
			while ((inputLine = in.readLine()) != null) {

				// if client sends
				if (inputLine.equals("JOIN") && joined) {
					joined = false;
					System.out.println(inputLine);
					out.println("Please enter your name");
					inputLine = in.readLine();
					
					// if their name is already in use ask them again
					while (Join(inputLine)) {
						out.println("The name is already in use please enter a different one.");
						inputLine = in.readLine();
					}
					name = inputLine;
					out.println("Welcome: " + name);
					// deals with when they lkeave
				} else if (inputLine.equals("LEAVE") && !joined) {
					joined = true;
					System.out.println(inputLine);
					Leave(name);
					out.println(inputLine);

					// returns the array
				} else if (inputLine.equals("LIST")) {
					System.out.println(inputLine);

					out.println(user);

					System.out.println(user);
					// when client doesn't use the right commands tell them
				} else {
					out.println("Invalid command");
				}

			}
			// close socket
			System.out.println(name + " has disconnected");
			Leave(name);
			Tsocket.close();

		} catch (IOException e) {

		}
	}

	public static void main(String[] args) throws IOException {

		int numconnect = 0;
		if (args.length != 1) {
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);

		try {
			ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
			while (true) {
				clientSocket = serverSocket.accept();
				Thread test = new Thread(new EchoServer(clientSocket.getInetAddress().getHostName()));
			}

		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}

}
