/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package org.apache.polygene.envisage.detail;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.apache.polygene.envisage.event.LinkEvent;
import org.apache.polygene.envisage.util.TableRow;
import org.apache.polygene.tools.model.descriptor.LayerDetailDescriptor;
import org.apache.polygene.tools.model.descriptor.ModuleDetailDescriptor;
import org.apache.polygene.tools.model.descriptor.ServiceDetailDescriptor;
import org.apache.polygene.tools.model.util.DescriptorUtilities;

/**
 * API would be defined as "All service interfaces which are visible for layer
 * or application" and SPI would be defined as "All service dependencies which
 * are not satisfied from within the module". And then similar for Layer. This
 * way you can select a Module/Layer and immediately see what it produces as
 * output and requires as input. This is also very very nice for documentation
 * purposes.
 */
/* package */ final class APIPane
    extends DetailPane
{
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle( APIPane.class.getName() );

    private JPanel contentPane;
    private JTable apiTable;
    private APITableModel apiTableModel;

    private Object linkObject;
    private Cursor defaultCursor;
    private Cursor linkCursor;

    /* package */ APIPane( DetailModelPane detailModelPane )
    {
        super( detailModelPane );
        this.setLayout( new BorderLayout() );
        this.add( contentPane, BorderLayout.CENTER );

        apiTableModel = new APITableModel();
        apiTable.setModel( apiTableModel );
        apiTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

        TableColumnModel columnModel = apiTable.getColumnModel();
        columnModel.getColumn( 0 ).setCellRenderer( new ServiceCellRenderer() );

        defaultCursor = getCursor();
        linkCursor = LinkEvent.LINK_CURSOR;

        MouseInputAdapter mouseInputListener = new MouseInputAdapter()
        {
            @Override
            public void mouseMoved( MouseEvent evt )
            {
                // Column 1 is the Service Column
                int col = apiTable.columnAtPoint( evt.getPoint() );
                if( col == 0 )
                {
                    setCursor( linkCursor );
                }
                else
                {
                    if( !getCursor().equals( defaultCursor ) )
                    {
                        setCursor( defaultCursor );
                    }
                }
            }

            @Override
            public void mouseClicked( MouseEvent evt )
            {
                int col = apiTable.columnAtPoint( evt.getPoint() );
                if( col != 0 )
                {
                    return;
                }

                int row = apiTable.rowAtPoint( evt.getPoint() );
                if( row < 0 )
                {
                    return;
                }

                linkObject = apiTableModel.getValueAt( row, col );
                linkActivated();
                linkObject = null;
            }
        };

        apiTable.addMouseMotionListener( mouseInputListener );
        apiTable.addMouseListener( mouseInputListener );
    }

    @Override
    protected void setDescriptor( Object objectDesciptor )
    {
        clear();

        List<ServiceDetailDescriptor> list = null;
        if( objectDesciptor instanceof LayerDetailDescriptor )
        {
            list = DescriptorUtilities.findLayerAPI( (LayerDetailDescriptor) objectDesciptor );
        }
        else if( objectDesciptor instanceof ModuleDetailDescriptor )
        {
            list = DescriptorUtilities.findModuleAPI( (ModuleDetailDescriptor) objectDesciptor );
        }

        if( list != null )
        {
            apiTableModel.addRow( list );
        }
    }

    private void clear()
    {
        linkObject = null;
        apiTableModel.clear();
    }

    private void linkActivated()
    {
        if( linkObject == null )
        {
            return;
        }
        LinkEvent linkEvt = new LinkEvent( this, linkObject );
        detailModelPane.fireLinkActivated( linkEvt );
    }

    
    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     */
    private void $$$setupUI$$$()
    {
        contentPane = new JPanel();
        contentPane.setLayout( new BorderLayout( 0, 0 ) );
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPane.add( scrollPane1, BorderLayout.CENTER );
        apiTable = new JTable();
        apiTable.setShowVerticalLines( true );
        scrollPane1.setViewportView( apiTable );
    }

    public JComponent $$$getRootComponent$$$()
    {
        return contentPane;
    }

    private static class APITableModel
        extends AbstractTableModel
    {
        /**
         * the column names for this model
         */
        private static final String[] COLUMN_NAMES =
        {
            BUNDLE.getString( "Service.Column" ),
            BUNDLE.getString( "Module.Column" ),
            BUNDLE.getString( "Visibility.Column" )
        };
        private final ArrayList<TableRow> rows;

        private APITableModel()
        {
            rows = new ArrayList<>();
        }

        private void addRow( List<ServiceDetailDescriptor> list )
        {
            if( list.isEmpty() )
            {
                return;
            }

            int i1 = rows.size();
            if( i1 > 0 )
            {
                i1--;
            }

            int i2 = 0;

            for( ServiceDetailDescriptor descriptor : list )
            {
                TableRow row = new TableRow( COLUMN_NAMES.length );
                row.set( 0, descriptor );
                row.set( 1, descriptor.module() );
                row.set( 2, descriptor.descriptor().visibility().toString() );
                this.rows.add( row );
                i2++;
            }

            fireTableRowsInserted( i1, i1 + i2 );
        }

        @Override
        public Object getValueAt( int rowIndex, int columnIndex )
        {
            TableRow row = rows.get( rowIndex );
            return row.get( columnIndex );
        }

        private void clear()
        {
            rows.clear();
            fireTableDataChanged();
        }

        @Override
        public int getColumnCount()
        {
            return COLUMN_NAMES.length;
        }

        @Override
        public String getColumnName( int col )
        {
            return COLUMN_NAMES[ col];
        }

        @Override
        public int getRowCount()
        {
            return rows.size();
        }
    }

    private static class ServiceCellRenderer
        extends DefaultTableCellRenderer
    {
        @Override
        public final Component getTableCellRendererComponent( JTable table,
                                                              Object value,
                                                              boolean isSelected,
                                                              boolean hasFocus,
                                                              int row,
                                                              int column
        )
        {
            if( value != null )
            {
                value = "<html><a href=\"" + value.toString() + "\">" + value.toString() + "</a></html>";
            }

            super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );

            return this;
        }
    }

}
