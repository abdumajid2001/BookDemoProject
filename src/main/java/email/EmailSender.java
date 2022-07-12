package email;

import com.nylas.*;
import responce.AppErrorDto;
import responce.DataDto;
import utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


public class EmailSender {
    private static String from = "abdumajidabdullatipov1@gmail.com";
    private static String password = "afglbhxnkavhuiri";

    public static void sendMessage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            entity.NylasEmail nylasEmail = Utils.readValue(req.getReader(), entity.NylasEmail.class);
            NylasClient nylasClient = new NylasClient();
            NylasAccount account = nylasClient.account("Tc99L2y1zZvndJ3Jd82s9igyfjHTtv");

            Draft draft = new Draft();
            draft.setSubject(nylasEmail.subject());
            draft.setBody(nylasEmail.body());
            draft.setTo(Arrays.asList(new NameEmail("test", nylasEmail.to())));
            account.drafts().send(draft);
            resp.getWriter().write(Utils.json().toJson(new DataDto<>("message sent successfully....")));
            resp.setStatus(200);
        } catch (Exception e) {
            resp.getWriter().write(Utils.json().toJson(new DataDto<>(new AppErrorDto("Xatolik yuz berdi", e.getMessage(), req.getRequestURL().toString()))));
            resp.setStatus(500);
        }

    }
}
