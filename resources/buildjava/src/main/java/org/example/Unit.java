package org.example;

/**
 * Hello world!
 *
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class Unit
{
    private String name;
    private List<String> topics;
    public Unit(String name, List<String> topics){
        this.name = name;
        this.topics = topics;
    }

    public String getName(){
        return this.name;
    }

    public List<String> getTopics(){
        return this.topics;
    }

    public static void main( String[] args )
    {
        List<String> topic = new ArrayList<String>();
        topic.add("Food");
        topic.add("Transport");
        topic.add("Entertainment");

        Unit testunit = new Unit("Bristol", topic);
//        System.out.println( "Hello World!" );
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.TEXT);
        resolver.setPrefix("templates/");
        
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
        
        Context c = new Context();
        c.setVariable("name", testunit.getName());
//        List<String> topics = Arrays.asList("Linux", "Git", "Maven");
        c.setVariable("topics", testunit.getTopics());
        String greeting = engine.process("unit", c);

        System.out.println(greeting);

    }
}
