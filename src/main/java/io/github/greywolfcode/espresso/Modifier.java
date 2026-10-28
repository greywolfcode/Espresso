package Parser;

public enum Modifier 
{
    //Access
    PUBLIC,
    PROTECTED,
    PACKAGE_PRIVATE,
    PRIVATE,

    //Class and Method
    FINAL,
    ABSTRACT,

    //Class only
    SEALED,
    NON_SEALED,

    //Method only
    STATIC,
    SYNCHRONIZED,
    NATIVE
}