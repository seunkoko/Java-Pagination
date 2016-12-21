 /*
 *  This program is a subsidiary of an application built to query a database 
 *  with thousands of contents that needs to be dynamically displayed in tens.
 *  Assumption --> The program already connects to the necessary database
 */

 public static void pagination(int size, javax.swing.JTable Table, DefaultTableModel model_method, int page_no){
        page_num = page_no;
        int new_page_no = 0;
        int remainder = size % 10;
        int quotient = size /10;
        int last_page_no = quotient+1;
        System.out.println(remainder + " " + quotient);
        
        try{
            db_connect = new db_connect();
            db_connect.connect_to_db();
            
            if(page_no == 1){
                statement = db_connect.con.prepareStatement("SELECT * from item LIMIT 0,10");
            }else{
                new_page_no = (page_no-1) * 10;
                statement = db_connect.con.prepareStatement("SELECT * from item LIMIT " + new_page_no + ",10");
            }
            
            if(new_page_no == last_page_no){
                lbl_double_forward.setForeground(Color.GRAY);
                lbl_forward.setForeground(Color.GRAY);
                lbl_back.setForeground(Color.black);
                lbl_double_back.setForeground(Color.black);
            }
            
            System.out.println(statement);
            rs = statement.executeQuery();
                    
            String[] column_name = {"SN", "Barcode", "Itemcode", "  Item   ", "Selling price", 
                                   "Cost price", "Quantity", "CategoryID", "SupplyName"};
            model_method = (DefaultTableModel) Table.getModel();
            model_method.setColumnIdentifiers(column_name);
            model_method.setRowCount(0);
                    
            java.sql.ResultSetMetaData rsmd = rs.getMetaData();
            int colNo = rsmd.getColumnCount();
                    
            while(rs.next()){
                Object[] objects = new Object[colNo];
                for(int i=0;i<colNo;i++){
                    objects[i]=rs.getObject(i+1);
                  }
                size++;
                model_method.addRow(objects);
            }
                  Table.setModel(model_method);
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }