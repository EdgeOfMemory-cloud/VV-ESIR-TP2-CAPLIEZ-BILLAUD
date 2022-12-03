# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer (Louis-Gabriel CAPLIEZ et Valère BILLAUD, ESIR2 Spé INFO, option SI)

Après utilisation de PMD sur le projet [Apache Commons Math](https://github.com/apache/commons-math), en utilisant l'ensemble de règles "rulesets/java/quickstart.xml, plusieurs problèmes sont soulevés.

Le repertoire ./ dans ce qui suit fait référence à la racine du projet Git.

### Vrai positif

Dans le fichier ./src/userguide/java/org/apache/commons/math4/userguide/LowDiscrepancyGeneratorComparison.java, ligne 182 un [switch case sans default est présent](https://pmd.sourceforge.io/pmd-6.51.0/pmd_rules_java_bestpractices.html#switchstmtsshouldhavedefault), l'erreur étant précisément *Switch statements should be exhaustive, add a default case (or missing enum branches)*:

```java
/***/
 for (Pair<String, RandomVectorGenerator> pair : generators) {
                    List<Vector2D> points = null;
                    int samples = datasets[type];
                    switch (type) {
                        case 0:
                            points = makeRandom(samples, pair.getValue());
                            break;
                        case 1:
                            points = makeRandom(samples, pair.getValue());
                            break;
                        case 2:
                            points = makeRandom(samples, pair.getValue());
                            break;
                        case 3:
                            points = makeCircle(samples, pair.getValue());
                            break;

                        // ici
                    }
                    add(new Plot(points), c);
                    c.gridx++;
                }
/***/
```
Nous pensons que PMD a eu raison de signaler cette erreur car le programmeur pourrait avoir oublié de penser à un cas par défaut, autrement dit un comportement non prévu de son switch. Il faudrait par sécurité en ajouter un pour que le programme s'exécute selon ce qu'il souhaite.
Une correction potentielle pourrait être :
```java
/***/
 for (Pair<String, RandomVectorGenerator> pair : generators) {
                    List<Vector2D> points = null;
                    int samples = datasets[type];
                    switch (type) {
                        case 0:
                            points = makeRandom(samples, pair.getValue());
                            break;
                        case 1:
                            points = makeRandom(samples, pair.getValue());
                            break;
                        case 2:
                            points = makeRandom(samples, pair.getValue());
                            break;
                        case 3:
                            points = makeCircle(samples, pair.getValue());
                            break;

                        default:
                            points = makeRandom(samples, pair.getValue()); // ou une ecriture dans un fichier pour signaler l'erreur (avec Log4J bien entendu =)).
                            break;
                    }
                    add(new Plot(points), c);
                    c.gridx++;
                }
/***/
```

### Faux positif

Dans le fichier ./src/userguide/java/org/apache/commons/math4/userguide/genetics/PolygonChromosome.java, ligne 106, des paranthèses inutiles sont signalées la règle [Useless parantheses](https://pmd.sourceforge.io/pmd-6.51.0/pmd_rules_java_codestyle.html#uselessparentheses) : 

```java
/***/
 public double fitness() {

        Graphics2D g2 = testImage.createGraphics();

        int width = testImage.getWidth();
        int height = testImage.getHeight();

        draw(g2, width, height);
        g2.dispose();

        int[] refPixels = refImage.getData().getPixels(0, 0, refImage.getWidth(), refImage.getHeight(), (int[]) null);
        int[] testPixels = testImage.getData().getPixels(0, 0, testImage.getWidth(), testImage.getHeight(), (int[]) null);

        int diff = 0;
        int p = width * height * 4 - 1; // 4 channels: rgba
        int idx = 0;

        do {
            if (idx++ % 4 != 0) { // ignore the alpha channel for fitness
                int dp = testPixels[p] - refPixels[p];
                if (dp < 0) {
                    diff -= dp;
                } else {
                    diff += dp;
                }
            }
        } while(--p > 0);

        return (1.0 - diff / (width * height * 3.0 * 256)); // ici 
    }
/***/
```

Les paranthèses signalées sont celles extérieures. Elles sont inutiles effectivement, mais elles disparaissent quoiqu'il arrive à la compilation et ne nuissent pas au comportement, à la sécurité/rapidité du programme à l'exécution. Elles aident plutôt à la lecture du code par un humain pour de gros calculs. Nous aurions donc tendance à les laisser.