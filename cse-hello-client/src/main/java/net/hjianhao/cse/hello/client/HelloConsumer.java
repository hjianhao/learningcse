/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.hjianhao.cse.hello.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.stereotype.Component;

@Component("helloConsumer")
public class HelloConsumer {
	@RpcReference(microserviceName = "business-service", schemaId = "hello")
	private Hello hello;

	public void hello() {
		hello.hello().whenComplete((result, exception) -> {
			if (exception == null) {
				String resultString = result;
				System.out.println("result : " + resultString);
			} else {
				System.out.println(exception);
				System.out.println(exception.getStackTrace());
			}
		});
		
	}

	public void invokeDelay() {
		List<CompletableFuture<String>> futures = new ArrayList<CompletableFuture<String>>();
		for (int i = 0; i < 10; ++i) {
			CompletableFuture<String> future = hello.delay(i).whenComplete((result, exception) -> {
				if (exception == null) {
					System.out.println("call succeeded!");
					System.out.println(result);
				} else {
					if (exception instanceof InvocationException) {
						InvocationException e = (InvocationException) exception;
						System.out.println(
								new StringBuilder().append("status code : ").append(e.getStatusCode()).toString());
						System.out.println(new StringBuilder().append("message : ").append(e.getMessage()).toString());
					} else {
						System.out.println(exception);
						System.out.println(exception.getStackTrace());
					}
				}
			});
			futures.add(future);
		}
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
				.whenComplete((result, exception) -> {
					System.out.println("all completed!");
				});
	}
}
