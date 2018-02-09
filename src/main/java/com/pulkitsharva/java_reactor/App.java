package com.pulkitsharva.java_reactor;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import reactor.core.publisher.Flux;

/**
 * Hello world!
 *
 */
public class App 
{
  @Test
  public void testNormalStream(){
    final List<String> input=new ArrayList<String>();
    Flux.just("String1", "String2", "String3").log().subscribe(new Subscriber<String>() {
      int count=0;
      public void onComplete() {
      }

      public void onError(Throwable arg0) {
      }

      public void onNext(String arg0) {
        if(count!=1)
          input.add(arg0);
        count++;
      }

      public void onSubscribe(Subscription arg0) {
        arg0.request(Long.MAX_VALUE);
      }});
    List <String> expectedOutput=new ArrayList<String>();
    expectedOutput.add("String1");
    expectedOutput.add("String3");
    assertArrayEquals(expectedOutput.toArray(),input.toArray());
  }
  
  @Test
  public void testBackPressureStream(){
    final List<String> input=new ArrayList<String>();
    Flux.just("String1", "String2", "String3").log().subscribe(new Subscriber<String>() {
      int count=0;
      private Subscription s;
      public void onComplete() {
      }

      public void onError(Throwable arg0) {
      }

      public void onNext(String arg0) {
        input.add(arg0);
        count++;
        if(count%2==0)
          s.request(2);
      }

      public void onSubscribe(Subscription arg0) {
        this.s=arg0;
        arg0.request(2);
      }});
    List <String> expectedOutput=new ArrayList<String>();
    expectedOutput.add("String1");
    expectedOutput.add("String2");
    expectedOutput.add("String3");
    assertArrayEquals(expectedOutput.toArray(),input.toArray());
  }
  
  
  @Test
  public void testZipStreams(){
    final List<String> input=new ArrayList<String>();
    Flux.just("String1", "String2", "String3").log()
    .zipWith(Flux.just("String4","String5","String6"),(firstFluxData, secondFluxData) -> firstFluxData+":"+secondFluxData)
    .subscribe(new Subscriber<String>() {
      public void onComplete() {
      }

      public void onError(Throwable arg0) {
      }

      public void onNext(String arg0) {
        input.add(arg0);
      }

      public void onSubscribe(Subscription arg0) {
        arg0.request(Long.MAX_VALUE);
      }});
    List <String> expectedOutput=new ArrayList<String>();
    expectedOutput.add("String1:String4");
    expectedOutput.add("String2:String5");
    expectedOutput.add("String3:String6");
    assertArrayEquals(expectedOutput.toArray(),input.toArray());
  }
    /*public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Flux.just("String1", "String2", "String3", "String4","String5","String6","String7","String8","String9","String10").log().subscribe(new Subscriber<String>() {
          int count=0;
          private Subscription s;
          public void onComplete() {
           System.out.println("all elements are processed");
            
          }

          public void onError(Throwable arg0) {
            System.out.println("error occurred"+arg0);
            
          }

          public void onNext(String arg0) {
            System.out.println("Trying to process something....."+arg0);
            count++;
            if(count%2==0){
              s.request(2);
            }
            
          }

          public void onSubscribe(Subscription arg0) {
            this.s=arg0;    
            arg0.request(2);
            
            // TODO Auto-generated method stub
            
          }});
        final List<Integer> elements = new ArrayList<Integer>();
        Flux.just(1, 2, 3, 4)
        .log()
        .subscribe(new Subscriber<Integer>() {
          public void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
            System.out.println("kuch mat kar");
          }
       
          public void onNext(Integer integer) {
            elements.add(integer);
          }
       
          public void onError(Throwable t) {}
       
          public void onComplete() {}
      });
        
        System.out.println(elements);
        
    }*/
}
