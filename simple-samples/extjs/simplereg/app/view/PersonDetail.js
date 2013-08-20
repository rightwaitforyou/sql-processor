/*
 * File: app/view/PersonDetail.js
 *
 * This file was generated by Sencha Architect version 2.2.2.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 4.2.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 4.2.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('Simplereg.view.PersonDetail', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.persondetail',

    requires: [
        'Simplereg.view.System',
        'Simplereg.view.PersonData',
        'Simplereg.view.PersonRelatives',
        'Simplereg.view.PersonContacts',
        'Simplereg.view.override.PersonDetail'
    ],

    itemId: 'person_detail',
    layout: {
        type: 'hbox'
    },
    bodyPadding: 5,
    closable: true,
    header: false,
    title: 'Person Detail',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    cls: 'thick',
                    itemId: 'main',
                    items: [
                        {
                            xtype: 'system'
                        },
                        {
                            xtype: 'tbseparator'
                        },
                        {
                            xtype: 'splitbutton',
                            action: 'search',
                            iconCls: 'icon-search',
                            text: 'Find',
                            menu: {
                                xtype: 'menu',
                                width: 120,
                                items: [
                                    {
                                        xtype: 'menuitem',
                                        action: 'search-all',
                                        text: 'Clear Filter'
                                    }
                                ]
                            }
                        },
                        {
                            xtype: 'button',
                            action: 'reload',
                            iconCls: 'icon-refresh',
                            text: 'Refresh'
                        },
                        {
                            xtype: 'tbseparator'
                        },
                        {
                            xtype: 'button',
                            action: 'create',
                            iconCls: 'icon-add',
                            text: 'Add'
                        },
                        {
                            xtype: 'button',
                            action: 'delete',
                            iconCls: 'icon-delete',
                            text: 'Remove'
                        },
                        {
                            xtype: 'tbfill'
                        },
                        {
                            xtype: 'splitbutton',
                            action: 'close',
                            iconCls: 'icon-page-close',
                            text: 'Close',
                            menu: {
                                xtype: 'menu',
                                items: [
                                    {
                                        xtype: 'menuitem',
                                        action: 'close-all',
                                        text: 'Close All'
                                    },
                                    {
                                        xtype: 'menuitem',
                                        action: 'close-other',
                                        text: 'Close All but Active'
                                    }
                                ]
                            }
                        }
                    ]
                }
            ],
            items: [
                {
                    xtype: 'persondata',
                    margin: 5,
                    flex: 1
                },
                {
                    xtype: 'personrelatives',
                    margin: 5,
                    flex: 1
                },
                {
                    xtype: 'personcontacts',
                    margin: 5,
                    flex: 1
                }
            ]
        });

        me.callParent(arguments);
    }

});