### Objective

This project outlines relevant thought that may go into implementing a simple annotation processor and attempts to explain what guice does under the hood. The objective is to explain how annotation processing works in java and how can one get started with writing a custom annotation processor.

It is meant to be educational and is not at all the best implementation. For any queries, please feel free to  reach out to me at **singhvib@**
 

### Processor Rules

Our processor provides a basic implementation of @Singleton annotation which is bound by the following rules:
1. Annotation can only be applied to concrete classes.
2. Classes annotated with @Singleton has one empty public constructor.

### Improvements
1. Make Processor generic (Dont hardcode Singleton.class in process())
2. Client should be agnostic to generated classes (should not need to be aware of StringUtilsSingleton)

