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

package io.github.greywolfcode.espresso;

import java.util.List;

abstract class Dclr
{
    interface Visitor<R>
    {
        public R visitPackageDclr(Package dclr);
        public R visitImportDclr(Import dclr);
        public R visitClassDclr(Class dclr);
    }

    public abstract <R> R accept(Visitor<R> visitor);
    static class Package extends Dclr
    {
        private final String packageName;

        public Package(String packageName)
        {
            this.packageName = packageName;
        }
        @Override
        public <R> R accept(Visitor<R> visitor)
        {
            return visitor.visitPackageDclr(this);
        }
        public String getPackageName()
        {
            return this.packageName;
        }
    }
    static class Import extends Dclr
    {
        private final String importName;

        public Import(String importName)
        {
            this.importName = importName;
        }
        @Override
        public <R> R accept(Visitor<R> visitor)
        {
            return visitor.visitImportDclr(this);
        }
        public String getImportName()
        {
            return this.importName;
        }
    }
    static class Class extends Dclr
    {
        private final List<Stmt> statements;

        public Class(List<Stmt> statements)
        {
            this.statements = statements;
        }
        @Override
        public <R> R accept(Visitor<R> visitor)
        {
            return visitor.visitClassDclr(this);
        }
        public List<Stmt> getStatements()
        {
            return this.statements;
        }
    }
}