/*
 * File: app/view/PersonDelete.js
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

Ext.define('SimpleWeb.view.person.Delete', {
    extend : 'Ext.window.Window',

    id : 'PersonDelete',
    width : 400,
    closeAction : 'hide',
    iconCls : 'icon-delete',
    title : 'Delete Person',
    modal : true,
    y : 100,

    initComponent : function() {
        var me = this;

        Ext.applyIf(me, {
            items : [ {
                xtype : 'form',
                bodyPadding : 10,
                header : false,
                title : 'Personal Data',
                defaults : {
                    anchor : '100%'
                },
                api : {
                    submit : 'simpleService.deletePerson'
                },
                items : [ {
                    xtype : 'textfield',
                    hidden : true,
                    name : 'id',
                    itemId : 'id',
                    fieldLabel : 'id'
                }, {
                    xtype : 'textfield',
                    hidden : true,
                    name : 'version',
                    itemId : 'version',
                    fieldLabel : 'Version'
                }, {
                    xtype : 'displayfield',
                    itemId : 'firstName',
                    name : 'firstName',
                    fieldLabel : 'First Name'
                }, {
                    xtype : 'displayfield',
                    itemId : 'lastName',
                    name : 'lastName',
                    fieldLabel : 'Last Name'
                }, {
                    xtype : 'displayfield',
                    renderer : function(value, displayField) {
                        return Ext.util.Format.dateRenderer('d.m.Y')(value)
                    },
                    itemId : 'dateOfBirth',
                    name : 'dateOfBirth',
                    fieldLabel : 'Date of Birth'
                }, {
                    xtype : 'displayfield',
                    itemId : 'ssn',
                    name : 'ssn',
                    fieldLabel : 'SSN'
                }, {
                    xtype : 'displayfield',
                    itemId : 'gender',
                    name : 'gender',
                    fieldLabel : 'Gender'
                } ]
            } ],
            dockedItems : [ {
                xtype : 'toolbar',
                dock : 'bottom',
                items : [ {
                    xtype : 'tbfill'
                }, {
                    xtype : 'button',
                    itemId : 'cancel_dialog',
                    text : 'Cancel'
                }, {
                    xtype : 'button',
                    itemId : 'accept_delete_person',
                    iconCls : 'icon-accept',
                    text : 'Delete Person'
                } ]
            } ]
        });

        me.callParent(arguments);
    }

});