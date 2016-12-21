 /*
 *  This program is a subsidiary of an application built to query a database 
 *  with thousands of contents in order to search effectively through this 
 *  database on text-field key-release thereby giving it a dynamic flavour.
 *  Assumption --> The program already connects to the necessary database
 */

 private void txt_field_searchKeyReleased(java.awt.event.KeyEvent evt) {                                             
        String search = txt_field_search.getText();
        PreparedStatement stmt;
        ResultSet resultset;
        DefaultTableModel model1= new DefaultTableModel();
        
        try{
                    size = 0;
                    // connection to the database and retrieval of data
                    db_connect = new db_connect();
                    db_connect.connect_to_db();
                    
                    stmt = db_connect.con.prepareStatement("SELECT * FROM item WHERE Concat (ID, Barcode,"
                            + "Itemcode, Item, Selling_price, Cost_price, Quantity, CategoryID, SuppplyName) "
                            + "LIKE '%" + search +"%' COLLATE latin1_swedish_ci");
                    System.out.println(stmt);
                    resultset = stmt.executeQuery(); 
                    
                    // display of the results retrieved from the database
                    if(search.equals("")){  
                        start_frame();  // if the txt_field_search is empty, populate the frame with the initialized values
                    }else if(!resultset.isBeforeFirst()){   // if the resultset returned is empty execute this part
                        initialize_numbers(false);  // method in the main program to initialize the numbers for pagination
                        String[] column_name = {"SN", "Barcode", "Itemcode", "  Item   ", "Selling price", 
                                        "Cost price", "Quantity", "CategoryID", "SupplyName"};
                        model1 = (DefaultTableModel) jTable1.getModel();
                        model1.setColumnIdentifiers(column_name);
                        model1.setRowCount(0);
                        int colNo = column_name.length;
                        Object[] objects = new Object[colNo];
                        String column_four = "";
                        //  this logic displays item not found in the middle of the frame targetting the column in the middle
                        for(int i=0;i<colNo;i++){
                         objects[i]="";
                         if(i == 4){
                             column_four = search + " was not found";
                             objects[i]= column_four;
                         }
                        }
                        // displays the "item was not found" on the frame
                        jTable1.getColumnModel().getColumn(4).setPreferredWidth(250);
                         model1.addRow(objects);
                         jTable1.setModel(model1);
                    }else{
                       // if the resultset returned is not empty execute this part
                       String[] column_name = {"SN", "Barcode", "Itemcode", "  Item   ", "Selling price", 
                                        "Cost price", "Quantity", "CategoryID", "SupplyName"};
                    model1 = (DefaultTableModel) jTable1.getModel();
                    model1.setColumnIdentifiers(column_name);
                    model1.setRowCount(0);
                    
                    java.sql.ResultSetMetaData rsmd = resultset.getMetaData();
                    int colNo = rsmd.getColumnCount();
                    while(resultset.next()){
                    Object[] objects = new Object[colNo];
                    for(int i=0;i<colNo;i++){
                         objects[i]=resultset.getObject(i+1);
                     }
                    size++;
                    model1.addRow(objects);
                    
                    }
                    initialize_numbers(false);
                    jTable1.setModel(model1);
                    
                    for(int i=0; i<jTable1.getColumnCount(); i++){
                        Class<?> col_class = jTable1.getColumnClass(i);
                        jTable1.setDefaultEditor(col_class, null);
                    } 
                    }
                }catch(SQLException err){
                    // displays error if the database connection fails
                    System.out.println("error: " + err.getMessage());
                }
}