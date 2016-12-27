new AJS.RestfulTable({
    autoFocus: true,
    el: jQuery("#feature-battles-table"),
    allowReorder: true,
    resources: {
        all: "rest/feature_battles",
        self: "rest/feature_battle"
    },
    columns: [
        {
            id: "status",
            header: ""
        },
        {
            id: "name",
            header: AJS.I18n.getText("common.words.name")
        },
        {
            id: "description",
            header: AJS.I18n.getText("common.words.description")
        },
        {
            id: "releaseDate",
            header: AJS.I18n.getText("version.releasedate")
        }
    ]
});