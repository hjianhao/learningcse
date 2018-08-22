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

package net.hjianhao.cse.hello.server;

import java.util.concurrent.CompletableFuture;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.swagger.invocation.context.ContextUtils;
import org.apache.servicecomb.swagger.invocation.context.InvocationContext;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;

import io.vertx.core.Vertx;
import net.sf.json.JSONObject;

@RestSchema(schemaId = "hello")
@Path("/")
public class HelloImpl {

  @Path("/hello")
  @GET
  public String hello() {
    return "Hello World!";
  }
  
  @Path("/delay")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public CompletableFuture<String> delay(@QueryParam("ms") @DefaultValue("0") long milliseconds) {
	  CompletableFuture<String> future = new CompletableFuture<String>();
	  
	  Vertx vertx = Vertx.currentContext().owner();
	  
	  if (milliseconds <= 0) {
//		  InvocationContext context = ContextUtils.getInvocationContext();
//		  context.setStatus(400);
//		  JSONObject jsonObj = new JSONObject ();
//		  jsonObj.put("code", 400);
//		  jsonObj.put("message", "invalid delay parameter!");
//		  
//		  future.complete(jsonObj.toString());
		  future.completeExceptionally(new InvocationException(Status.BAD_REQUEST, "invalid delay parameter!"));
		  return future;
	  }
	  
	  vertx.setTimer (milliseconds, id -> {
		  JSONObject jsonObj = new JSONObject ();
		  jsonObj.put("delay", milliseconds);
		  jsonObj.put("message", "hello!");
		 future.complete(jsonObj.toString());
	  });
	  
	  return future;
  }  
}


