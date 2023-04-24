/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bookstorermi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.NoSuchElementException;
import javax.swing.JOptionPane;


public class Client {
    
    public static void main(String[] args){
        try{
            Registry reg = LocateRegistry.getRegistry("localhost",1099);
            BookFacade server = (BookFacade) reg.lookup("rmi://localhost/service");
            
            boolean findMore;
            do{
                String[] options = { "Show All", "Find a PRODUCT","add product", "Exit"};
                
                int choice = JOptionPane.showOptionDialog(null, "Choose an action", "Option dialog",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null, options, options[0]);
                
                switch(choice){
                    case 0:{
                        List<Book> list = server.getAllBooks();
                        
                        StringBuilder message = new StringBuilder();
                        list.forEach( x -> {
                            message.append(x.toString() + "\n");
                        });
                        JOptionPane.showMessageDialog(null, new String(message));
                        break;
                    }
                    case 1: {
                        String code = JOptionPane.showInputDialog("Type the code of the book you want to find");
                        try {
                            Book response = server.findBook(new Book(code));

                            JOptionPane.showMessageDialog(null, "titre :"
                                    + response.getTitle() +"\n"+"quantité:"
                                    +response.getQty(),
                                    response.getCode(), JOptionPane.INFORMATION_MESSAGE);
                            
                        } catch (NoSuchElementException ex) {
                            JOptionPane.showMessageDialog(null, "Not found");
                        }
                        break;
                    }
                    // end case 1:
                    case 2: {
                       
                        String title = JOptionPane.showInputDialog("Enter the title of the new PRODUCT");
                        String code = JOptionPane.showInputDialog("Enter the code of the new book");
                        
                        double qty = Double.parseDouble(JOptionPane.showInputDialog("Enter the qty of the new PROCUT"));
                        

                        Book newBook = new Book(title,code,qty);
                        server.addBook(newBook);

                        JOptionPane.showMessageDialog(null, "The new book has been added successfully!");

                        break;
                    }
                    default:
                        System.exit(0);
                        break;
                }
                findMore = (JOptionPane.showConfirmDialog(null, "Do you want to exit?","Exit",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION);
                
            }while(findMore);
        
        }catch(RemoteException | NotBoundException e){
            System.out.println(e.getMessage());
        }
    }
    
}
