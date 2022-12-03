# Extending PMD

Use XPath to define a new rule for PMD to prevent complex code. The rule should detect the use of three or more nested `if` statements in Java programs so it can detect patterns like the following:

```Java
if (...) {
    ...
    if (...) {
        ...
        if (...) {
            ....
        }
    }

}
```
Notice that the nested `if`s may not be direct children of the outer `if`s. They may be written, for example, inside a `for` loop or any other statement.
Write below the XML definition of your rule.

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](https://github.com/selabs-ur1/VV-ESIR-TP2/blob/master/exercises/designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer (Louis-Gabriel CAPLIEZ et Valère BILLAUD, ESIR2 Spé INFO, option SI)

Notre règle au format au format XML est la suivante:

```xml
<rule name="Nested_If"
      language="java"
      message="Il y a au moins 3 Ifs!!!!!!!!!"
      class="net.sourceforge.pmd.lang.rule.XPathRule">
   <priority>1</priority>
   <properties>
      <property name="version" value="2.0"/>
      <property name="xpath">
         <value>
<![CDATA[
//IfStatement 
//IfStatement
//IfStatement
]]>
         </value>
      </property>
   </properties>
</rule>
```
Sur la classe de test suivante écrite par nos soins :

```java
public class ifs3 {

    public void f1() {
	if (true) {
	    int i = 1; // pas détecté, ok
	}
    }

    public void f2() {
	if (true) {
	    int i = 1;
	    if (i == 2) { // pas détecté, ok
		int ab = 2;
	    }
	}
    }

    public void f3() {
	if (true) {
	    int i = 1;
	    if (i == 2) {
		int ab = 2;
	    } else {
		if (i == 5000) { // détecté, ok
		int valere = 2;
		}
	    }
	}
    }

    public void f3_for() {
	if (true) {
	    int i = 1;
	    if (i == 2) {
		int ab = 2;
		for (int j = 0; j < 5000; j++) {
		    int dec = 1234;
		    if (i == 2) { // détecté, ok
			int c = 2;
		    }
		}
	    }
	}
    }
    
}
```
le troisième if imbriqué (et même à l'intérieur d'une boucle) des méthodes f3() et f3_for() sont détectées par PMD mais pas le simple if de la méthode f1() et le double if de la méthode f2().
Cependant si nous ajoutons un appel à f3() dans le if de f1(), il n'y a aucun triples ifs imbriqués de détectés, alors que f3() en contient. La règle serait donc à préciser un peu plus.

Aucun triples ifs imbriqués n'est détecté dans le projet [Apache Commons Math](https://github.com/apache/commons-math/tree/master/src/userguide/java/org/apache/commons/math4/userguide), cela est à vérifier avec la remarque précédente sur des appels de méthodes imbriqués les uns dans les autres.