import net.proteanit.sql.DbUtils;
//Libraries dealing with graphics.
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.*;


public class Library {
    private JPanel MAIN;
    private JTextField txtName;
    private JTextField txtYear;
    private JTextField txtNumber;
    private JTextField txtAuthor;
    private JTextField searchField;
    private JTextField txtCopies;
    private JButton saveButton;
    private JTable table1;
    private JButton deleteButton;
    private JButton UPDATEButton;
    private JButton SEARCHButton;

    private JScrollPane databaseTable;


//entry point of the program
    public static void main(String[] args) {
        JFrame frame = new JFrame("Library");
        frame.setContentPane(new Library().MAIN);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setVisible(true);
    }
    // The Connection interface represents a connection to a database
    Connection con;
    //The PreparedStatement is used to execute parameterized SQL queries and updates.
    PreparedStatement pst;

    public void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/librarybookdetails", "root","");
            System.out.println("Database connected successfully!");
        }
        catch(SQLException e){
            // It provides information about the exception, including the error message and the sequence of method calls that led to the exception.
            e.printStackTrace();
        }
    }
    //This line defines a method named tableLoader() with a void return type. It indicates that the method does not return any value.
    void tableLoader(){
        try {
            pst = con.prepareStatement("select * from bookdatabase");
            //This line executes the SQL query using the executeQuery() method of the
            // PreparedStatement object (pst).It returns a ResultSet object (results) containing the retrieved data
            ResultSet results = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(results));//displays the retrieved values in table1.
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Library() {
        connect();
        tableLoader();
        saveButton.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
            String bookName, bookAuthor, ISBN, publicationYear, copies;

            bookName = txtName.getText();
            bookAuthor = txtAuthor.getText();
            ISBN = txtNumber.getText();
            publicationYear = txtYear.getText();
            copies = txtCopies.getText();
            //If statement checks if any of the text fields are empty. If any field is empty, it displays a message dialog
                // asking the user to fill in a complete form.
            if (txtName.getText().equals("") || txtAuthor.getText().equals("") || txtNumber.getText().equals("") || txtYear.getText().equals("") || txtCopies.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Please fill in a complete form!");
            }
            else {
                try {
                    pst = con.prepareStatement("insert into bookdatabase(bookName, bookAuthor,ISBN, publicationYear,copies)values(?,?,?,?,?)");
                    //These lines sets the values for the parameters in the prepared statement.
                    pst.setString(1, bookName);
                    pst.setString(2, bookAuthor);
                    pst.setString(3, ISBN);
                    pst.setString(4, publicationYear);
                    pst.setString(5, copies);
                    if (bookName == bookName) {
                        JOptionPane.showMessageDialog(null, "Book already Exists! Try changing number of copies");
                    }

                    else {
                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Record Added successfully!");
                        tableLoader();
                        txtName.setText("");
                        txtAuthor.setText("");
                        txtNumber.setText("");
                        txtYear.setText("");
                        txtCopies.setText("");
                        txtName.requestFocus();
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    });

        SEARCHButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //This line retrieves the text from the searchField JTextField component and assigns it to the bookID variable.
                    String bookID = searchField.getText();
                    if (searchField.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please specify the book number before searching!");
                    }

                    else {
                        pst = con.prepareStatement("select bookName, bookAuthor,ISBN, publicationYear,copies from bookdatabase where Book = ?");
                        pst.setString(1, bookID);
                        ResultSet results = pst.executeQuery();

                        if (results.next()) {
                            String libraryBookName = results.getString(1);
                            String libraryBookAuthor = results.getString(2);
                            String libraryISBN = results.getString(3);
                            String libraryPublicationYear = results.getString(4);
                            String libraryCopies = results.getString(5);

                            //The retrieved values are then used to set the text of the corresponding JTextFields
                            // (txtName, txtAuthor, txtNumber, txtYear, txtCopies) to display the book details.
                            txtName.setText(libraryBookName);
                            txtAuthor.setText(libraryBookAuthor);
                            txtNumber.setText(libraryISBN);
                            txtYear.setText(libraryPublicationYear);
                            txtCopies.setText(libraryCopies);
                        } else {
                            txtName.setText("");
                            txtAuthor.setText("");
                            txtNumber.setText("");
                            txtYear.setText("");
                            txtCopies.setText("");
                            JOptionPane.showMessageDialog(null, "Invalid book Number");
                        }
                    }
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        });

        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String bookName, bookAuthor, ISBN, publicationYear, copies, Bookid;

                bookName = txtName.getText();
                bookAuthor = txtAuthor.getText();
                ISBN = txtNumber.getText();
                publicationYear = txtYear.getText();
                copies = txtCopies.getText();
                Bookid = searchField.getText();

                if(searchField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please search the book number before Updating!");
                }

                else if (txtName.getText().equals("") || txtAuthor.getText().equals("") || txtNumber.getText().equals("") || txtYear.getText().equals("") || txtCopies.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please fill in a complete form before Updating!");
                }

                //The else block executes if the search field and all text fields are filled.
                // It prepares and executes an SQL update statement to update the book details in the database based on the provided book number.
                else {
                    try {
                        pst = con.prepareStatement("update bookdatabase set bookName = ?, bookAuthor = ?,ISBN = ?, publicationYear = ?, copies = ? where Book = ?");
                        pst.setString(1, bookName);
                        pst.setString(2, bookAuthor);
                        pst.setString(3, ISBN);
                        pst.setString(4, publicationYear);
                        pst.setString(5, copies);
                        pst.setString(6, Bookid);

                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Record Updated!");
                        tableLoader();
                        txtName.setText("");
                        txtAuthor.setText("");
                        txtNumber.setText("");
                        txtYear.setText("");
                        txtCopies.setText("");
                        txtName.requestFocus();

                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Bookid;
                Bookid = searchField.getText();

                if(searchField.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"Please search a book number before deleting!");
                }

                else {
                    try {
                        pst = con.prepareStatement("delete from bookdatabase where Book = ?");
                        pst.setString(1, Bookid);//This line sets the value for the parameter in the prepared statement using the Bookid variable.
                        pst.executeUpdate();//executes the SQL delete statement. After the deletion is performed successfully, it displays a success message,
                        // calls tableLoader() to refresh the table data, clears the text fields, and sets the focus on the txtName text field.
                        JOptionPane.showMessageDialog(null, "Record Deleted!");
                        tableLoader();
                        txtName.setText("");
                        txtAuthor.setText("");
                        txtNumber.setText("");
                        txtYear.setText("");
                        txtCopies.setText("");
                        txtName.requestFocus();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

    }
}