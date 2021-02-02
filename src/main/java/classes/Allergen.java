/**
 @Auther Ian Holderness 14023756

 */

package com.example.foodallergyapp.App.classes;

// Creates the object Allergens with the different parts
public class Allergen {
   private String type;
   private String allergenDisc;
   private int allergenID;
   private boolean state;

   // called when creating a new Allergen object setting each allergen
   public Allergen(String allergenDisc, String type, int allergenID, boolean state){
       this.type = type;
       this.allergenDisc = allergenDisc;
       this.allergenID = allergenID;
       this.state = state;
   }
    //used to get/ set the different parts of teh Allergen object
    public String getAllergenDisc() {
        return allergenDisc;
    }
    //
    public int getAllergenID() {
        return allergenID;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
