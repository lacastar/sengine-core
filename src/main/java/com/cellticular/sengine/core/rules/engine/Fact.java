/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cellticular.sengine.core.rules.engine;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Szenthe László
 */
@Data
@Builder
public class Fact {
    String name;
    Object value;
}
