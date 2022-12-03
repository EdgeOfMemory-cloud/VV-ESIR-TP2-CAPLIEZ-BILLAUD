# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer (Louis-Gabriel CAPLIEZ et Valère BILLAUD, ESIR2 Spé INFO, option SI)

### Définitions

Les sources utilisées pour cette question sont [celle-ci](https://www.aivosto.com/project/help/pm-oo-cohesion.html#TCC_LCC) et celle-ci [pages 37 et 38](https://savoirs.usherbrooke.ca/bitstream/handle/11143/1606/MR83692.pdf).

Les connexions s'entendent au niveau des méthodes d'une même classe.

Une connexion est dite directe pour deux méthodes si, soit elles accèdent tous deux à la même variable au niveau de la classe (variable statique ou attribut), ou alors en naviguant dans l'arbre d'appel commencant à chacune des deux fonctions, elles accèdent à la même variable au niveau de la classe (variable statique ou attribut). Dans ce cas les procédures privées sont prises en compte, cependant un appel de méthode en dehors de la classe ne l'est pas.

Si ces deux méthodes ne sont pas directement connectées, mais par une ou plusieurs méthodes intermédiaires, elles sont indirectement connectées (par transitivité).

Si **n** est le nombre de méthodes existantes, le **N**ombre maximum de connexions **P**ossibles est **NP = n * (n - 1) / 2**.

TCC est définie par le rapport entre le **N**ombre **D**irect de **C**onnexions d'une classe et le **N**ombre maximum de connexions **P**ossibles, soit **TCC = NDC / NP**.

LCC est définie par le rapport entre le **N**ombre **D**irect de **C**onnexions d'une classe plus le **N**ombre **I**ndirect de **C**onnexions et le **N**ombre maximum de connexions **P**ossibles, 
soit **LCC = (NDC + NIC) / NP**.

TCC et LCC peuvent être égaux dans plusieurs situations :
1. si TCC = LCC = 0, aucune méthode de classe n'est connectée à une autre, la cohésion est nulle.
2. si TCC = LCC < 1, toutes les connexions existantes dans la classe sont directes (NIC = 0), cependant d'autres connexions sont possibles.
3. si TCC = LCC = 1, alors NIC = 0 et toutes les méthodes sont directement connectées entre elles. Cohésion maximale.

### Exemple (TCC = LCC = 1, cohésion maximale)

```java
public class ClassB {

    int attribut1;

    int attribut2;

    public ClassB() {
        this.attribut1 = 0;
        this.attribut2 = 0;
    }

	public void meth1() {	
	
		int c = attribut1;
        attribut2 = 3;
	}
	
	public void meth2() {
		
        attribut1 = 6;
        attribut2 = 10;
	}
	
	public void meth3() {	
		
        int d = attribut2;
        int e = attribut1;
	}
}
```
### Est-ce que LCC peut être inférieur à TCC ?

En reprenant les deux formules **TCC = NDC / NP** et **LCC = (NDC + NIC) / NP**, nous pouvons remarquer que NDC + NIC sera toujours supérieur >= NDC, donc il est mathématiquement impossible que LCC soit < TCC. Dit autrement, la condition pour être connectée directement est plus restrictive (NDC) que pour être indirectement connectée (NIC).   