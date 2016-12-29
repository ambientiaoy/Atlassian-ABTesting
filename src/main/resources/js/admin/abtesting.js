var baseUrl = contextPath + "/rest/abtesting/1.0";

AJS.toInit(function() {
    new AJS.RestfulTable({
        autoFocus: true,
        el: jQuery("#feature-battles-table"),
        allowReorder: true,
        resources: {
            all: baseUrl + "/feature_battles",
            self: baseUrl + "/feature_battle"
        },
        columns: [
            {
                id: "id",
                header: ""
            },
            {
                id: "threshold",
                header: AJS.I18n.getText("feature.battle.threshold")
            },
            {
                id: "goodOld",
                header: AJS.I18n.getText("feature.battle.goodold")
            },
            {
                id: "newAndShiny",
                header: AJS.I18n.getText("feature.battle.newandshiny")
            }
        ]
    });
});