/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosqldetection;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Black_Knight
 */
public class FeatureExtractor {

    public String query;
    public String tempQuery;
    public String whitespace_chars;
    public String whitespace_charclass;

    public FeatureExtractor(String query) {
        this.query = query;
        //System.out.println(query);
        
        whitespace_chars = "" /* dummy empty string for homogeneity */
                + "\\u0009" // CHARACTER TABULATION
                + "\\u000A" // LINE FEED (LF)
                + "\\u000B" // LINE TABULATION
                + "\\u000C" // FORM FEED (FF)
                + "\\u000D" // CARRIAGE RETURN (CR)
                + "\\u0020" // SPACE
                + "\\u0085" // NEXT LINE (NEL) 
                + "\\u00A0" // NO-BREAK SPACE
                + "\\u1680" // OGHAM SPACE MARK
                + "\\u180E" // MONGOLIAN VOWEL SEPARATOR
                + "\\u2000" // EN QUAD 
                + "\\u2001" // EM QUAD 
                + "\\u2002" // EN SPACE
                + "\\u2003" // EM SPACE
                + "\\u2004" // THREE-PER-EM SPACE
                + "\\u2005" // FOUR-PER-EM SPACE
                + "\\u2006" // SIX-PER-EM SPACE
                + "\\u2007" // FIGURE SPACE
                + "\\u2008" // PUNCTUATION SPACE
                + "\\u2009" // THIN SPACE
                + "\\u200A" // HAIR SPACE
                + "\\u2028" // LINE SEPARATOR
                + "\\u2029" // PARAGRAPH SEPARATOR
                + "\\u202F" // NARROW NO-BREAK SPACE
                + "\\u205F" // MEDIUM MATHEMATICAL SPACE
                + "\\u3000" // IDEOGRAPHIC SPACE
                ;
        /* A \s that actually works for Java’s native character set: Unicode */
        whitespace_charclass = "[" + whitespace_chars + "]";
        
        tempQuery = query.replaceAll(whitespace_charclass + "+", "");

    }

    public int isNullComparison() {
        if (query.contains("null")) {
            return 1;
        }
        return 0;
    }

    public int containsEmptyString() {
        if (query.contains("\"\"") || query.contains("''")) {
            return 1;
        } else {
            return 0;
        }
    }
    
    public int containsRegexTrue()
    {
        if(tempQuery.contains("/.*/") || tempQuery.contains("/./") || tempQuery.contains("/.")) return 1;
        return 0;
    }
    
    public int containsEndQuery()
    {
        if(tempQuery.contains(";}//")) return 1;
        return 0;
    }

    /**
     * Needs to work with this. Remove all white spaces from both query and
     * payload string, then look for match
     *
     * @return
     */
    public int containsPayload() {
        try {
            Scanner input = new Scanner(new File("payload.txt"));
            while(input.hasNext())
            {
                String temp = input.nextLine();
                temp = temp.replaceAll(whitespace_charclass, "");
                if(tempQuery.contains(temp)) return 1;
                //System.out.println(temp);
            }
            //System.out.println("----------");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FeatureExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public int containsNotEqual()
    {
        if(query.contains("$ne")) return 1;
        return 0;
    }
    
    public int doesTargetTable()
    {
        if(query.contains("createTable()") || query.contains("showTable()")) return 1;
        return 0;
    }
    
    public int doesAlterCollection()
    {
        String str[] = query.split("createCollection\\(");
        if(str.length>1) return 1;
        if(query.contains("drop(")) return 1;
        return 0;
    }
    
    public int doesDropDatabase()
    {
        if(query.contains("dropDatabase(")) return 1;
        return 0;
    }

    public int elementQueryOperations(){
        if(tempQuery.contains("$exists") || tempQuery.contains("$type")) return 1;
        return 0;

    }

    public int evalQueryOperations(){
        if (tempQuery.contains("$mod") || tempQuery.contains("$regex") || tempQuery.contains("$text") || tempQuery.contains("$where")) return 1;
        return 0;
    }

    public int containsExtremes(){
        if(tempQuery.contains("zzz")) return 1;
        //if (tempQuery.contains("99999999")) return 1;
        if(tempQuery.contains("~")) return 1;

        return  0;
    }
    
    public int containsCondition()
    {
        if(tempQuery.contains("find(") || tempQuery.contains("$selector")) return 1;
        if(tempQuery.contains("find.sort(")) return 1;
        if(tempQuery.contains("$eq") || tempQuery.contains("$gt") || tempQuery.contains("$gte") || tempQuery.contains("$lt") || tempQuery.contains("$lte") || tempQuery.contains("$ne") || tempQuery.contains("$in") || tempQuery.contains("$nin")) return 1;
        return 0;
    }
    
    public int containsLogicalOp()
    {
        if(query.contains("$or") || query.contains("$and") || query.contains("$not") || query.contains("$nor")) return 1;
        return 0;
    }
    
    public int containsJS()
    {
        if(query.contains("$where")) return 1;
        else return 0;
    }
    
    public int containsReturn()
    {
        if(query.contains(";return") || query.contains("return 1") || query.contains("return true") || query.contains("return(true)")) return 1;
        else return 0;
    }
    
    public int isUpdateQuery()
    {
        if(tempQuery.contains("update(") || tempQuery.contains("save(")) return 1;
        return 0;
    }
    
    public int isRemoveQuery()
    {
        if(tempQuery.contains("remove(")) return 1;
        return 0;
    }
    
    /**
    public int isProjectionQuery()
    {
        //if(tempQuery.matches("(.+)\\.(.+)\\.find\\({.*},{\\\".*\\.\\$\\\":1.*}\\)")) return 1;
        return 0;
    }
    */

    public int containsLimit()
    {
        if(query.contains("limit")) return 1;
        return 0;
    }

    public int containsEmptyArray() {
        if (query.contains("{}") || query.contains("[]")) {
            return 1;
        } else {
            return 0;
        }
    }

    public int isNewQuery() {
        if (query.contains(";db.")) {
            return 1;
        } else {
            return 0;
        }
    }

    public int isWhileTrue() {
        if (query.contains("while(true)")) {
            return 1;
        } else {
            return 0;
        }
    }
}
