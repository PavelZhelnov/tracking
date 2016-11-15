<head>
    <style type="text/css" xmlns="http://www.w3.org/1999/html">
        .ReadMsgBody {
            width: 100%;
        }

        .ExternalClass {
            width: 100%;
        }

        .nav .yshortcuts {
            color: #666666
        }

        .blacklink .yshortcuts {
            color: #000000
        }

        .graylink .yshortcuts {
            color: #999999
        }
    </style>
</head>
<body>
<div>

    <label>${dateFromTo}</label>
    <br/>
    <br/>
    <label>Dear <b>${storeId}</b> store management,

    This is a reminder that for all orders listed below, an email reminder will be sent to customers on <b>${date}</b>
    informing them to pick up their alteration orders. In the event that some or all of the orders listed below have
    already been picked up, please update the status of those orders.

    Sincerely,
    Men's Wearhouse Store Operations</label></div>
<br/>
<table id="reportTable" class="table" style="padding-top: 5px;">
    <thead>
    <tr style="background-color: #1a1a1a; color: #ffffff">
        <th>Order Id</th>
        <th>Status</th>
        <th>Customer</th>
        <th>Completed date</th>
        <th>Ready by Date</th>
    </tr>
    </thead>
    <tbody>
    <#list orders as o>
    <tr>
        <td>${o.orderId}</td>
        <td>${o.status}</td>
        <td>${o.customer.lastName} ${o.customer.firstName}</td>
        <td>
            <#if o.completeDateTime??>${o.completeDateTime}</#if>
        </td>
        <td>
            <#if o.dueDateTime??>${o.dueDateTime}</#if>
        </td>
    </tr>
    </#list>
    </tbody>
</table>
</body>