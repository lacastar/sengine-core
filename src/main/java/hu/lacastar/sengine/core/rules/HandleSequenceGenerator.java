/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.lacastar.sengine.core.rules;

/**
 *
 * @author Szenthe László
 */
public class HandleSequenceGenerator {
    long sequence = Long.MIN_VALUE;
    long getNextVal(){
        return sequence++;
    }
    void resetSequence(){
        sequence = Long.MIN_VALUE;
    }
}
