# Autodoc
A framework for producing system description as a set of linked facts and building live visualisation of them.

## Idea
We believe that:
* Knowledge about some complex system can be represented as an ontology: a set of triplets about relations 
between system components. Like this: "Account Service (module) --- runs on --- node05.compute.local (host)"
* Collection of such facts may be represented graphically with a very limited set visual artifacts and concepts - 
 (a shape, an arrow, a label) and a concept of composition.
* When people share knowledge about systems they mostly use this small set of graphical primitives drawing them 
 on marker board, sheet of paper or other surface and add some commentary and labels. We can do the same.
* We can go further as we have more power with ability to make diagrams live, clickable, expandable. We can add 
 interactive decomposition and filtering
*  We can make system documentation and visualization a part of development process just like writing unit tests.
 Writing docs about systems architecture and implementation details may become just a common practice in development 
 process culture.
* We can make diagrams show current events in system like errors, components going up and down, messages delivered and
 CPU utilization.
 We can use a diagram as a time machine showing what was happening to our system last week or yesterday. 
 
 Currently writing docs and drawing diagrams is a pain because everybody knows that the doc will become obsolete 
 very quickly. And this is some practice detached from development itself. We go documenting something when we have 
 time to or when we fell that the system became too complex to manage and we have problems with building common 
 understanding of how things work between team members.
 
 But if the diagram draws itself and is always actual, we are in a new wonderful world. 
 Effort to build such a diagram should be not very big (simpler than write a set of unit tests) and let one build 
 system description incrementally, step by step.  

Currently we can draw something like this:
![example](Screenshot%202018-09-29%2003.10.27.png)


## Current status

Implementing first proof-of-concept examples for Spring applications.

## Usage
Check autodoc-example-java for samples

```
HashMap<String, Object> context = new HashMap<String, Object>();
context.put("applicationContext", applicationContext);
AutodocJavaEngine doc = new AutodocJavaEngine(context);
doc.describeSpringApplication(applicationContext, new FileStorage("structure.log"));
```

then run translation from raw data to GoJS "ViewModel".
```
java org.stropa.autodoc.translate.TranslatorToGoJS structure.log data/gojs-model.json
```

open `gojs.html`

## What was done

Defined the concept of architecture. 

Played with some Javascript libs to select the best for drawing diagrams with decomposition (expanding nodes). 
The winner is GoJS (https://gojs.net/latest/index.html). Also looked at D3.js

Implemented first version of "ViewModel" for GoJS and html page to draw diagram from static data

