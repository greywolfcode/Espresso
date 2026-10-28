/*
 * Espresso Compiler
 * Copyright (C) 2026  greywolfcode
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package io.github.greywolfcode.espresso.tools;

import java.io.IOException;
import java.io.PrintWriter;

import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;

public class GenerateAst 
{
    private static String indent = "    ";

    public static void main(String[] args) throws IOException
    {
        if (args.length != 1)
        {
            System.err.println("Usage: generate_ast <output_directory>");
            System.exit(64);
        }
        String outputDir = args[0];

        generateAst(outputDir, "Dclr", //declarations
            new Type[] {
                new Type(
                    "Package",
                    new Field[] {
                        new Field("String", "packageName")
                    } //Field[]
                ), //Type
                new Type(
                    "Import",
                    new Field[] {
                        new Field("String", "importName")
                    } //Field[]
                ), //Type
                new Type(
                    "Class",
                    new Field[] {
                        new Field("List<Modifier>", "modifiers"),
                        new Field("String", "name"),
                        new Field("String", "superclass"),
                        new Field("List<String>", "interfaces"),
                        new Field("List<Dclr>", "declarations"),
                    } //Field[]
                ), //Type
                        new Field("List<Stmt>", "statements"),
                    } //Field[]
                ) //Type
            } //Type[]
        );
        generateAst(outputDir, "Stmt", //Statements
            new Type[] {
            } //Type[]
        );
    }
    private static void generateAst(String outputDir, String name, Type[] types) throws IOException
    {
        Path path = Path.of(outputDir, name + ".java");
        String packageName = path.getParent().toString().replace("/", ".").replace("\\", ".");
        PrintWriter writer = new PrintWriter(path.toString(), "UTF-8");

        writer.println("package " + packageName + ";");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + name);
        writer.println("{");

        defineVisitor(writer, name, types);

        writer.println();
        writer.println(indent + "public abstract <R> R accept(Visitor<R> visitor);");

        for (Type type : types)
        {
            defineClass(writer, name, type);
        }

        writer.println("}");
        writer.close();
    }
    private static void defineVisitor(PrintWriter writer, String base, Type[] types)
    {
        writer.println(indent + "interface Visitor<R>");
        writer.println(indent + "{");

        for (Type type : types)
        {
            writer.println(indent + indent + "public R visit" + type.getName() + base + "(" + type.getName() + " " + base.toLowerCase() + ");");
        }
        writer.println(indent + "}");

    }
    private static void defineClass(PrintWriter writer, String base, Type type)
    {
        writer.println(indent + "static class " + type.getName() + " extends " + base);
        writer.println(indent + "{");

        List<String> fields = new ArrayList<String>();

        //fields
        for (Field field : type.getFields())
        {
            fields.add(field.getType() + " " + field.getName());
            writer.println(indent + indent + "private final " + field.getType() + " " + field.getName() + ";");
        }
        writer.println();

        //constructor
        writer.println(indent + indent + "public " + type.getName() + "(" + String.join(", ", fields) + ")");
        writer.println(indent + indent + "{");

        for (Field field : type.getFields())
        {
            writer.println(indent + indent + indent + "this." + field.getName() + " = " + field.getName() + ";");
        } 

        writer.println(indent + indent + "}");

        //visitor pattern
        writer.println(indent + indent + "@Override");
        writer.println(indent + indent + "public <R> R accept(Visitor<R> visitor)");
        writer.println(indent + indent + "{");
        writer.println(indent + indent + indent + "return visitor.visit" + type.getName() + base + "(this);");
        writer.println(indent + indent + "}");


        //getters
        for (Field field : type.getFields())
        {
            writer.println(indent + indent + "public " + field.getType() + " get" + toCaps(field.getName()) + "()");
            writer.println(indent + indent + "{");
            writer.println(indent + indent + indent + "return this." + field.getName() + ";");
            writer.println(indent +indent + "}"); 
        }

        writer.println(indent + "}");
    }
    private static String toCaps(String val)
    {
        return val.substring(0, 1).toUpperCase() + val.substring(1); 
    }

}

//Storage Classes
class Field
{
    private final String type;
    private final String name;

    public Field(String pType, String pName)
    {
        type = pType;
        name = pName;
    }
    public String getType()
    {
        return type;
    }
    public String getName()
    {
        return name;
    }
}
class Type 
{
    private final String name;
    private final Field[] fields;

    public Type(String pName, Field[] pFields)
    {
        name = pName;
        fields = pFields;
    }
    public String getName()
    {
        return name;
    }
    public Field[] getFields()
    {
        return fields;
    }
}