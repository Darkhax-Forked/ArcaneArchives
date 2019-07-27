### Class

```java
import mods.arcanearchives.GCT;
```

#### Methods

```java
static void addRecipe(
  string name,         // the recipe name
  IItemStack output,   // the output as an itemstack
  IIngredient[] inputs // the inputs as an array of ingredients
);
```


---


```java
static void removeRecipe(
  IItemStack output // the output itemstack to be removed (quantity must match)
);
```


---


### Examples

```java
import mods.arcanearchives.GCT;

// Removes the recipe for radiant dust
GCT.removeRecipe(<arcanearchives:radiant_dust>*2);

// Adds a new recipe for radiant dust
GCT.addRecipe("radiant_dust", <arcanearchives:radiant_dust>*2, [<minecraft:flint>, <arcanearchives:raw_quartz>]);
```