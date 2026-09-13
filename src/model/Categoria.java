/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
/**
 * Enumera as categorias possíveis para uma {@link Tarefa}, cada uma com
 * uma descrição textual associada.
 * @author 2025001861
 */
public enum Categoria {
    ANTIBIOTICO ("Antibiotico"), 
    SUPLEMENTO ("Suplemento"),
    USO_CONTINUO ("Uso Continuo");
    
    String uso;
    
    /**
     * @param uso descrição textual da categoria
     */
    private Categoria (String uso){
        this.uso = uso;
    }
    
    /**
     * @return a descrição textual da categoria
     */
    public String getUso(){
        return uso;
    }
    
}
