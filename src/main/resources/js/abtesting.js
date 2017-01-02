var baseUrl = contextPath + "/rest/abtesting/1.0";

AJS.toInit(function() {

    AJS.$(".featurebattle-decision").click( function( event ){
        var button = $(this);
        var user = button.data("user");
        var fb  = button.data("feature-battle");
        var experiment = button.data("experiment");
        AJS.$.ajax({
            url: baseUrl + "/feature_battle/" + fb + "/users.json",
            contentType: "application/json",
            type: "POST",
            dataType: "json",
            data: JSON.stringify ({"type": experiment, "user": user}),
        }).done( function (data, status, jqXHR) {
            alert ( status );
            alert ( data );
        });
    });
});