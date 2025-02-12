![Logo](https://github.com/amblelabs/modkit/blob/main/src/main/resources/assets/kit_logo.png?raw=true)

# Amble Kit Library
## This a libary mod that can be used for code simplifying for developing on Fabric
***This is used in many of our Fabric mods***

## What does this add?

### Minecraft Registration
Instead of having to manually register each and every thing, you can simple `extend` or `implement` one of our `RegistryContainer` classes.

These utility interfaces are recognised by our mod by sticking `RegistryContainer.register(ClassName.class, MOD_ID` into your mods `#onInitialize` method.

### Datapack Workflow
We provide a custom class called `SimpleDatapackRegistry`

This allows your own classes to be read and registered straight from datapacks with ease!

For your registry to be recognised by the kit, you need to implement `SakitusModInitializer` ( Fabric Endpoint )

Then, listen to the `RegistryEvents.INIT.register` event and call `registries.register(MyRegistry.getInstanace())` and... Done! ( SUBJECT TO IMPROVEMENT )

### Data Generation
We utilise annotations and the previously mentioned registry containers to automatically generate many features.

For example, automatic english translation for blocks - 

By simply creating an instance of `AmbleLanguageProvider` and passing in your `BlockContainer` with the `#withBlocks` method, next time you run datagen all these blocks will have english translations based off their identifiers.

There are more datagen utilities akin to this.

### Much more!
