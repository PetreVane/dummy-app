package com.dummy.application.views.ec2info;

import com.dummy.application.services.Ec2Info;
import com.dummy.application.views.MainLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

@PageTitle("Ec2Info")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class Ec2InfoView extends VerticalLayout {

    private final static Logger logger  = Logger.getLogger(Ec2InfoView.class.getName());
    private H3 ipLabel; // = new H3("EC2 Instance Information");


    public Ec2InfoView() {
        setSpacing(false);

        Image img = new Image("images/aws-image.png", "aws logo");
        img.setWidth("200px");
        add(img);

        ipLabel= new H3("The IP and AZ details will be filled in soon");
        ipLabel.addClassNames(Margin.Top.LARGE, Margin.Bottom.MEDIUM);
        add(ipLabel);

        add(new Paragraph("Hopefully this will be filled with some actual information soon 🤗"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        getEC2Info();
    }

    private void getEC2Info()
    {
        CompletableFuture.supplyAsync( () ->
        {
            try
            {
                Map<String, String> ec2Info = Ec2Info.getPublicInfo();
                return "IP address: " + ec2Info.get("ip") + "<br>" + "Availability Zone: " + ec2Info.get("az") + "<br>";
            }
            catch (IOException | InterruptedException e)
            {
                logger.log(Level.SEVERE, "Failed to fetch ec2 info", e);
                return "Failed to fetch ec2 info: " + e.getMessage(); // "Failed to fetch ec2 info: java.io.IOException: Connection timed out (Connection timed out)"
            }
        }).thenAccept( (info) -> getUI().ifPresent(ui -> ui.access( () -> ipLabel.getElement().setProperty("innerHTML", info))));
    }


}
