<#assign top_nav_selected = "status">
<#assign page_title = "Status">
<#include "../header.ftl"/>
<div>
    <h3>Application is running. Version: ${version}</h3>
    <hr/>
    <hr/>
    <div id="monitoringTests">
        <span id="errorMessage" style="color: #d14"></span>

        <table class="table table-bordered" style="width: 100%;">
            <tr>
                <th>Test</th>
                <th></th>
            </tr>
        <#list tests?keys as testid>
            <tr>
                <td>${tests[testid]}</span></td>
                <td>
                    <button id="${testid}" name="startTest" type="button" class="run-test btn btn-mini btn-info">
                        Run
                    </button>
                    <img id="loading${testid}" style="display:none;" src="${contextPath}/img/ajax-loader.gif"
                         alt="Loading..."/>

                </td>
            </tr>
        </#list>
        </table>
        <button type="submit" id="allTests" class="btn btn-info">Start all tests</button>
    </div>

    <table id="testResults" class="table table-bordered">
        <thead>
        <tr style="background-color: #1a1a1a; color: #ffffff">
            <th>Test</th>
            <th width="70%">Result</th>
            <th>Response time(ms)</th>
            <th>Success</th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>

<script type="text/javascript">

    $(document).ready(function () {

        $(".run-test").click(function () {
            dataTable.fnClearTable();
            startTest($(this)[0].id);
        });
        $("#allTests").click(function () {
            dataTable.fnClearTable();
        <#list tests?keys as testid>
            startTest('${testid}');
        </#list>
        });
        dataTable = $("#testResults").dataTable();
    });

    function startTest(testId) {
        var loading = $("#loading" + testId);
        loading.toggle(true);
        $.ajax({
            type: 'GET',
            'url': "${contextPath}/tmw/monitoring/start?testId=" + testId,
            dataType: "text",
            success: function (result) {
                var json = $.parseJSON(result);
                initResults(json);
                loading.toggle(false);
            }, 'error': function (e) {
                if (e.errorMessage) {
                    $("#errorMessage").text(e.errorMessage != null ? e.errorMessage : "");
                } else {
                    $("#errorMessage").text(e.responseText != null ? e.responseText : "");
                }
                loading.toggle(false);
            }
        });

    }

    function initResults(data) {
        var results = data.results;
        $.each(results, function (key, value) {
            var label;
            if (value.success) {
                label = '<span class="label label-success">Success</span>';
            } else {
                label = '<span class="label label-important">Error</span>';
            }
            dataTable.fnAddData([value.desc, value.testResult, value.responseTime, label]);
        });

    }

</script>
<#include "../footer.ftl"/>
