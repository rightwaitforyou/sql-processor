Ext.define('SimpleWeb.controller.Person', {
    extend : 'Ext.app.Controller',

    statics : {
        setData : function(record, view) {
            var key, field;
            for (key in record.data) {
                field = view.down("#" + key);
                if (field) {
                    field.setValue(record.data[key]);
                }
            }
        }
    },

    init : function() {

        Ext.create("SimpleWeb.view.person.Search");
        Ext.create("SimpleWeb.view.person.Edit");
        Ext.create("SimpleWeb.view.person.Add");
        Ext.create("SimpleWeb.view.person.Delete");
        Ext.create("SimpleWeb.view.contact.Add");
        Ext.create("SimpleWeb.view.contact.Edit");
        Ext.create("SimpleWeb.view.contact.Delete");

        this.control({
            "#cancel_dialog" : {
                click : this.onCancelDialogClick
            },
            "#person_list" : {
                itemdblclick : this.onPersonListDblClick
            },
            "#modify_person" : {
                click : this.onModifyPersonClick
            },
            "#accept_modify_person" : {
                click : this.onAcceptModifyPersonClick
            },
            "#add_person" : {
                click : this.onAddPersonClick
            },
            "#accept_add_person" : {
                click : this.onAcceptAddPersonClick
            },
            "#delete_person" : {
                click : this.onDeletePersonClick
            },
            "#accept_delete_person" : {
                click : this.onAcceptDeletePersonClick
            },
            "#search_person" : {
                click : this.onSearchPersonClick
            },
            "#accept_search_person" : {
                click : this.onAcceptSearchPersonClick
            },
            "#all_person" : {
                click : this.onAllPersonClick
            },
            "#contact_list" : {
                itemclick : this.onContactListClick,
                itemdblclick : this.onContactListDblClick
            },
            "#add_contact" : {
                click : this.onAddContactClick
            },
            "#accept_add_contact" : {
                click : this.onAcceptAddContactClick
            },
            "#modify_contact" : {
                click : this.onModifyContactClick
            },
            "#accept_modify_contact" : {
                click : this.onAcceptModifyContactClick
            },
            "#delete_contact" : {
                click : this.onDeleteContactClick
            },
            "#accept_delete_contact" : {
                click : this.onAcceptDeleteContactClick
            },
            "#clear_dialog" : {
                click : this.onClearDialogClick
            },
            "#refresh_person" : {
                click : this.onRefreshPersonClick
            },
            "#refresh_person_detail" : {
                click : this.onRefreshPersonDetailClick
            }
        });
    },

    onCancelDialogClick : function(button, e, eOpts) {
        console.log("onCancelDialogClick");
        button.up("window").close();
    },

    onPersonListDblClick : function(dataview, record, item, index, e, eOpts) {
        console.log("onPersonListDblClick");
        this.buildDetails(record.data.id);
    },

    onContactListClick : function(dataview, record, item, index, e, eOpts) {
        console.log("onContactListClick");
        this.toggleContactEditButton(true);
        this.toggleContactDeleteButton(true);
    },

    toggleContactEditButton : function(enable) {
        var panel = Ext.getCmp("PersonRegistry");
        var view = panel.getActiveTab();
        var button = view.down('#modify_contact');
        if (enable) {
            button.enable();
        } else {
            button.disable();
        }
    },

    toggleContactDeleteButton : function(enable) {
        var panel = Ext.getCmp("PersonRegistry");
        var view = panel.getActiveTab();
        var button = view.down('#delete_contact');
        if (enable) {
            button.enable();
        } else {
            button.disable();
        }
    },

    buildDetails : function(id) {
        console.log("buildDetails ", id);
        var storeId = "person" + id;

        var uniqueStore = Ext.data.StoreManager.lookup(storeId);
        if (!uniqueStore) {
            uniqueStore = Ext.create("SimpleWeb.store.People", {
                storeId : storeId
            });
            console.log("new store ", storeId);
            console.log(uniqueStore);
        } else {
            console.log("old store ", storeId);
        }

        uniqueStore.load({
            params : {
                id : id,
                contacts : true
            },
            scope : this,
            callback : function(records, operation, success) {
                console.log(records);
                var person = records[0];
                console.log("loaded person ", person, person.contacts());
                this.showDetails(person);
            }
        });

    },

    showDetails : function(record) {
        console.log("showDetails");
        var itemId = "person" + record.data.id
        var panel = Ext.getCmp("PersonRegistry");
        // Get tab
        var view = panel.child("#" + itemId);
        if (!view) {
            console.log("new details");
            view = Ext.create("SimpleWeb.view.person.Details", {
                store : record.contacts(),
                itemId : itemId,
                closable : true
            });
            panel.add(view);
        } else {
            console.log("old details");
            var store = view.down("gridpanel").getStore();
            store.loadData(record.contacts().data.items);
        }

        // Set title
        var title = record.data.firstName + " " + record.data.lastName;
        if (record.data.ssn)
            title += " | " + record.data.ssn;
        view.setTitle(title);

        // Set data
        SimpleWeb.controller.Person.setData(record, view);

        // Reference...
        view.record = record;
        // Finish
        panel.setActiveTab(view);
    },

    onRefreshPersonDetailClick : function(button, e, eOpts) {
        console.log("onRefreshPersonDetailClick");
        var el = button;

        while (el = el.up()) {
            if (el.record) {
                this.buildDetails(el.record.data.id);
                break;
            }
        }
    },

    onAddPersonClick : function(button, e, eOpts) {
        console.log("onAddPersonClick");
        Ext.getCmp("PersonAdd").show();
    },

    onAcceptAddPersonClick : function(button, e, eOpts) {
        console.log("onAcceptAddPersonClick");
        var dialog = Ext.getCmp("PersonAdd");
        var form = dialog.down("form");
        var values = form.getValues();

        if (form.isValid()) {
            console.log(values);
            form.submit({
                scope : this,
                success : function(form, action) {
                    this.doGridRefresh();
                    dialog.close();
                    this.buildDetails(action.result.id);
                    form.reset();
                },
                failure : function(form, action) {
                    console.log(action.result);
                }

            });
        }
    },

    onModifyPersonClick : function(button, e, eOpts) {
        console.log("onModifyPersonClick");
        var dialog = Ext.getCmp("PersonModify");
        var form = dialog.down("form");
        var el = button;

        while (el = el.up()) {
            if (el.record) {
                form.loadRecord(el.record);
                dialog.show();
                break;
            }
        }
    },

    onAcceptModifyPersonClick : function(button, e, eOpts) {
        console.log("onAcceptModifyPersonClick");
        var dialog = Ext.getCmp("PersonModify");
        var form = dialog.down("form");
        var values = form.getValues();

        if (form.isValid()) {
            console.log(values);
            form.submit({
                scope : this,
                success : function(form, action) {
                    this.doGridRefresh();
                    dialog.close();
                    var person = form.getRecord();
                    person.set(values); 
                    person.data.version = action.result.version;
                    this.showDetails(person);
                },
                failure : function(form, action) {
                    console.log(action.result);
                }

            });
        }
    },

    onDeletePersonClick : function(button, e, eOpts) {
        console.log("onDeletePersonClick");
        var dialog = Ext.getCmp("PersonDelete");
        var form = dialog.down("form");
        var el = button;

        while (el = el.up()) {
            if (el.record) {
                form.loadRecord(el.record);
                dialog.show();
                break;
            }
        }
    },

    onAcceptDeletePersonClick : function(button, e, eOpts) {
        console.log("onAcceptDeletePersonClick");
        var dialog = Ext.getCmp("PersonDelete");
        var form = dialog.down("form");
        var values = form.getValues();

        console.log(values);
        form.submit({
            scope : this,
            success : function(form, action) {
                this.doGridRefresh();
                dialog.close();
                var panel = Ext.getCmp("PersonRegistry");
                var view = panel.child("#person" + values.id);
                view.close();
            },
            failure : function(form, action) {
                console.log(action.result);
            }
        });
    },

    onSearchPersonClick : function() {
        console.log("onSearchPersonClick");
        Ext.getCmp("PersonSearch").show();
    },

    onAcceptSearchPersonClick : function(button) {
        console.log("onAcceptSearchPersonClick");
        var dialog = Ext.getCmp("PersonSearch");
        var form = dialog.down('form');
        var store = this.getStore("People");
        console.log(form.getValues());

        store.load({
            params : form.getValues(),
            callback : function(records, operation, success) {
                console.log(records);
            },
            scope : this
        });
        dialog.close();
    },

    onAllPersonClick : function(button) {
        console.log("onAllPersonClick");
        var store = this.getStore("People");

        store.load({
            callback : function(records, operation, success) {
                console.log(records);
            },
            scope : this
        });
    },

    doGridRefresh : function() {
        console.log("doGridRefresh");
        var panel = Ext.getCmp("PersonRegistry");
        var list = panel.down('pagingtoolbar');
        list.doRefresh();
    },

    showContactDialog : function(panel, dialog, record) {
        console.log("showContactDialog ", panel);
        if (panel) {
            var storeId = panel.getItemId();
            if (record) {
                dialog.down("form").loadRecord(record);
            }
            dialog.down("#person_id").setValue(storeId);
            dialog.down("#personId").setValue(storeId.substring(6));
            dialog.show();
        }
    },

    reloadContacts : function(dialog, id) {
        console.log("reloadContacts ", dialog, id);
        var storeId = dialog.down("#person_id").getValue();
        var personId = dialog.down("#personId").getValue();
        var store = Ext.data.StoreManager.lookup(storeId);
        var person = store.first();
        person.contacts().load({
            params : {
                personId : personId
            // id : id
            },
            callback : function(records, operation, success) {
                if (!operation.exception) {
                    console.log(records);
                    person.contacts().add(records);
                    dialog.close();
                }
            },
            scope : this
        });
    },

    onAddContactClick : function(button, e, eOpts) {
        console.log("onAddContactClick");
        var dialog = Ext.getCmp("ContactAdd");
        var panel = button.up('panel').up('panel');
        this.showContactDialog(panel, dialog);
    },

    onAcceptAddContactClick : function(button, e, eOpts) {
        console.log("onAcceptAddContactClick");
        var dialog = Ext.getCmp("ContactAdd");
        var form = dialog.down("form");
        var values = form.getValues();

        if (form.isValid()) {
            console.log(values);
            form.submit({
                scope : this,
                success : function(form, action) {
                    this.reloadContacts(dialog, action.result.id);
                    form.reset();
                },
                failure : function(form, action) {
                    console.log(action.result);
                }

            });
        }
    },

    onModifyContactClick : function(button, e, eOpts) {
        console.log("onModifyContactClick");
        var dialog = Ext.getCmp("ContactModify");
        var grid = button.up("panel").down("gridpanel");
        var record = grid.getSelectionModel().getSelection()[0];
        var panel = button.up('panel').up('panel');
        this.showContactDialog(panel, dialog, record);
    },

    onContactListDblClick : function(dataview, record, item, index, e, eOpts) {
        console.log("onContactListDoubleClick");
        var dialog = Ext.getCmp("ContactModify");
        var panel = dataview.up('panel').up('panel').up('panel');
        this.showContactDialog(panel, dialog, record);
    },

    onAcceptModifyContactClick : function(button, e, eOpts) {
        console.log("onAcceptModifyContactClick");
        var dialog = Ext.getCmp("ContactModify");
        var form = dialog.down("form");
        var values = form.getValues();

        if (form.isValid()) {
            console.log(values);
            form.submit({
                scope : this,
                success : function(form, action) {
                    this.reloadContacts(dialog, action.result.id);
                    var contact = form.getRecord();
                    contact.set(values);
                    contact.data.version = action.result.version;
                },
                failure : function(form, action) {
                    console.log(action.result);
                }

            });
        }
    },

    onDeleteContactClick : function(button, e, eOpts) {
        console.log("onDeleteContactClick");
        var dialog = Ext.getCmp("ContactDelete");
        var grid = button.up("panel").down("gridpanel");
        var record = grid.getSelectionModel().getSelection()[0];
        var panel = button.up('panel').up('panel');
        this.showContactDialog(panel, dialog, record);
    },

    onAcceptDeleteContactClick : function(button, e, eOpts) {
        console.log("onAcceptDeleteContactClick");
        var dialog = Ext.getCmp("ContactDelete");
        var form = dialog.down("form");
        var values = form.getValues();

        console.log(values);
        form.submit({
            scope : this,
            success : function(form, action) {
                this.reloadContacts(dialog, values.id);
            },
            failure : function(form, action) {
                console.log(action.result);
            }
        });
        this.toggleContactEditButton(false);
        this.toggleContactDeleteButton(false);
    },

    onClearDialogClick : function(button, e, eOpts) {
        console.log("onClearDialogClick");
        button.up("window").down("form").getForm().reset();
    },

    onRefreshPersonClick : function(button, e, eOpts) {
        console.log("onRefreshPersonClick");
        var dialog = Ext.getCmp("PersonSearch");
        var form = dialog.down('form');
        var store = this.getStore("People");
        console.log(form.getValues());

        store.load({
            params : form.getValues(),
            callback : function(records, operation, success) {
                console.log(records);
            },
            scope : this
        });
        dialog.close();
    }
});
