<#assign top_nav_selected = "dailySchedule">
<#assign page_title = "Daily Schedule">
<#include "*/header.ftl"/>
<fieldset>
    <legend>Daily Schedule:</legend>
    <form id="showOrderInfo" action="${contextPath}/tmw/storeInfo/dailyScheduleInfo" method="post">
        <div class="input-prepend input-append" style="position: relative;">
            <span class="add-on" style="height: 20px;width: 70px;">Store:</span>

            <div id="storeErrorMessage" class="control-group" style="position: absolute;top: 0;left:78px;">
                <div class="controls">
                    <input name="storeId" type="text" autocomplete="off" style="height: 30px;width: 156px;"
                           data-provide="typeahead" value="${storeId!""}"
                           data-source="[<#list stores as store>&quot;${store.uniqueId?string("0")}&quot;<#if store_has_next>,</#if></#list>]">
                    <span class="help-inline"></span>
                </div>
                {storeErrorMessage}
            </div>
        </div>
        <br/>

        <div style="position: relative;">
            <button type="submit" class="btn btn-info">Show</button>
        <#if currentDate??>
            <button style="position: absolute;right: 40px;top:15px;" class="btn btn-success" id="print">Print
            </button></#if>
        </div>

    </form>

<#if currentDate??>

    <form id="showEmailInfo" action="${contextPath}/tmw/storeInfo/usersForSendingSchedule" method="post">
        <input name="storeId" type="hidden" value="${storeId}"/>
        <button type="submit" class="btn btn-info">Email Schedule
        </button>
    </form>

    <#assign total = [] />
    <#assign totalGarmentsMinutes = [] />
    <#assign totalMinutes = 0 />
    <#list orders as order>
        <#assign totalOrder = [] />
        <#assign totalOrder = totalOrder + [0] />
        <#if order.orderTotalMinutes??>
            <#assign totalOrder = totalOrder + [order.orderTotalMinutes] />
            <#assign totalMinutes = totalMinutes + order.orderTotalMinutes />
        <#else>
            <#assign totalOrder = totalOrder + [0] />
            <#assign totalMinutes = totalMinutes + 0 />

        </#if>
        <#assign total = total + [totalOrder] />

    </#list>
    <!-- header -->
    <div style="position: relative;height:60px">
        <img src="${contextPath}/img/tailoringSchedule-icon.png"/><h4
            style="position: absolute;top:5px;left: 60px;">${currentDate?string("EEEE, MMMM dd, yyyy")}</h4>

        <div style="position: absolute;right: 40px;top:-20">
            <div><b>Total orders: ${orders?size}</b></div>
            <div><b>Total tailors: ${tailors}</b></div>
            <div><b>Total order minutes: ${totalMinutes}</b></div>
            <div><b>Tailor total minutes: ${tailorTotalMinutes}</b></div>
            <div><b>Available minutes: ${todayAvailableMinutes}</b></div>
        </div>
    </div>
<#--data-->
    <#setting time_zone="UTC">


    <!-- Daily Schedule Info Table -->
    <table id="dailyScheduleInfoTable" class="table table-bordered"
           style="padding-top: 5px; width: 100%;">
        <caption style="text-align:center;"><b>Today's work</b></caption>
        <thead>
        <tr style="background-color: #1a1a1a; color: #ffffff">
            <td>Rush</td>
            <th>Order ID</th>
            <th>Customer name</th>
            <th>Phone number</th>
            <th>Due date</th>
            <th>Garments</th>
            <th>Total (min.)</th>
            <th>NCA</th>
            <th>Overdue</th>
            <th>Tailor</th>
        </tr>
        </thead>

        <tbody>
            <#list orders as order>
                <#assign garmentSize = 0 />
                <#assign garmentSize = order.garments />
                <#assign orderInfo = total[order_index] />
            <tr <#if order.isExternal()>style="background-color: #ADFF2F"</#if>>
                <td>
                    <#if order.getRushOrder()>
                        <img src="${contextPath}/img/timer_red.svg" width="30px" height="30px"/>
                    </#if>
                </td>
                <td text-align="center">
                    # ${order.orderUniqueId?html}
                </td>
                <td text-align="center">
                ${order.lastName?html}, ${order.firstName?html}
                </td>
                <td text-align="center">
                 ${order.phone?html}
                </td>
                <td text-align="center">
                ${order.dueDate?string("EEE MM/dd hh:mm a")}
                </td>
                <td text-align="center">
                ${garmentSize}
                </td>
                <td text-align="center">
                ${order.orderTotalMinutes}
                </td>
                <td text-align="center">
                    <#if order.orderType=='NCA'>
                        NCA
                    </#if>
                </td>
                <td text-align="center">
                    <#if order.isOverdue()>
                        X
                    </#if>
                </td>
                <td text-align="center">
                </td>
            </tr>

            </#list>
            <#if !orders?has_content>
            <tr>
                <td colspan="9">
                </td>
            </tr>
            </#if>

        </tbody>
    </table>

    <table>
        <caption style="text-align:left; font-size: 12px;">Color mapping</caption>
        <tbody>
        <tr>
            <td width="40" style="background-color: #ADFF2F"<"/>
            <td style="text-align:left; font-size: 12px;">
                External order
            </td>
        </tr>
        <tr>
            <td width="40"><img src="${contextPath}/img/timer_red.svg" width="30px" height="30px"/></td>
            <td style="text-align:left; font-size: 12px;">
                Rush Order
            </td>


        </tr>
        </tbody>
    </table>

    <!-- Tailor Schedules Available Table -->
    <#if isTailorSchedulesAvailable>
        <table id="tailorSchedulesAvailableTable" class="table table-bordered table-striped"
               style="padding-top: 5px; width: 100%;">
            <caption style="text-align:center;"><b>Upcoming 14 days work</b></caption>
            <thead>
            <tr style="background-color: #1a1a1a; color: #ffffff">
                <th>Ready by</th>
                <th>Day of Week</th>
                <th># of orders</th>
                <th>Minutes required</th>
                <th>Tailor minutes</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
                <#list dateMap?keys as dateValue>
                <tr>
                    <td>
                    ${dateMap[dateValue]?string("d-MMM")}
                    </td>
                    <td>
                    ${dateMap[dateValue]?string("EEEE")}
                        <#if dateValue_index==0>
                            (today)
                        </#if>
                    </td>
                    <td>
                    ${ordersDueDateStat[dateValue]}
                    </td>
                    <td>
                    ${requiredTimeMap[dateValue]}
                    </td>
                    <td>
                    ${totalTailorTimeMap[dateValue]}
                    </td>
                    <td>
                    </td>

                </tr>
                </#list>
            </tbody>
        </table>
    <#else>
        <div align="left">
            <h4>
                Alert: No tailor schedules available on the following days.
                Please ensure schedules are posted in Empower or Peoplesoft: ${currentDate?string("EEEE")}
                , ${currentDate?string("dd/MM/yyyy")}
            </h4>
        </div>
    </#if>

</#if>
</fieldset>
<script type="text/javascript">

    $(document).ready(function () {
        $("#print").click(function () {
            $("#scheduleReport").printElement();
            //window.location.reload();
        });
    });
</script>
<#include "*/footer.ftl"/>
