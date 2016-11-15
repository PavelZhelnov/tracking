<#assign top_nav_selected = "usersInfo">
<#assign page_title = "Send Email to">
<#include "*/header.ftl"/>
<fieldset>
    <form id="dailySchedule" action="${contextPath}/tmw/storeInfo/dailyScheduleInfo" method="post">
        <input name="storeId" type="hidden" value="${storeId}"/>
        <button type="submit" name="dailySchedule" class="btn btn-info">Daily Schedule</button>
    </form>

    <form id="sendSchedule" action="${contextPath}/tmw/storeInfo/sendScheduleInfo" method="post">
        <input name="storeId" type="hidden" value="${storeId}"/>

        <div id="usersIsEmptyMessage" class="control-group">
            <button type="submit" name="sendSchedule" class="btn btn-info">Send Schedule</button>
            <span class="help-inline"></span>
        </div>

        <div align="left">
            <h5>Enter user short name or select user from list below</h5>
        </div>

        <div class="controls">
            <input name="user" type="text" autocomplete="off" style="height: 30px; width: 156px;"
                   data-provide="typeahead"
                   data-source="[<#list allUsers as user>&quot;${user.uniqueId}&quot;<#if user_has_next>,</#if></#list>]">
            <span class="help-inline"></span>
        </div>

        <span id="successMessage" style="color: #468847"></span>
        
        <!-- table contains users who can receive email -->
        <table id="usersInfoTable" class="table table-bordered table-striped"
               style="padding-top: 5px; width: 100%;">
            <caption style="text-align:center;"><b>Send Schedule to</b></caption>
            <tbody>
            <#list usersInfo as userInfo>
            <tr>
                <td text-align="center">
                    <b>${userInfo.lastName?html}, ${userInfo.firstName?html}</b>
                    <br>
                ${userInfo.uniqueId?html}
                </td>
                <td text-align="center">
                    <input type="checkbox" name="users" value="${userInfo.uniqueId}"/>
                </td>
            </tr>
            </#list>
            </tbody>
        </table>
    </form>
</fieldset>

<script type="text/javascript">
    $(document).ready(function () {
        $('#sendSchedule').submit(function () {
            $.ajax({
                type: 'POST',
                'url': $(this).attr('action'),
                'data': $(this).serialize(),
                'success': function (res) {
                    var json = $.parseJSON(res);
                    var success = $("#successMessage");
                    var usersIsEmpty = $("#usersIsEmptyMessage");
                    success.text(json.successMessage != null ? json.successMessage : "");
                    usersIsEmpty.toggleClass("error", json.usersIsEmptyMessage != null);
                    usersIsEmpty.find(".help-inline").text(json.usersIsEmptyMessage != null ? json.usersIsEmptyMessage : "");
                    success.fadeIn(3000);
                }
            });
            return false; // prevent default action
        });
    });
</script>

<#include "*/footer.ftl"/>
