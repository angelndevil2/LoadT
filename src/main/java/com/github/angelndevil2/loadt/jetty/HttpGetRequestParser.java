package com.github.angelndevil2.loadt.jetty;

import com.github.angelndevil2.loadt.common.LoadTException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author k, Created on 16. 2. 19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class HttpGetRequestParser extends UriParser {

    @Override
    public IRequest getRequest(@NonNull String uri) throws LoadTException {
        IRequest req = new HttpGetRequest();

        String[] parsed = parseOne(uri);
        if (!parsed[0].equals("LoadT")) {
            throw new LoadTException("invalid uri");
        }

        if (parsed[1].equals(ServiceUri.Uri4Get.INFO)) req.setTarget(ServiceUri.Uri4Get.INFO);

        else if (parsed[1].equals(ServiceUri.Uri4Get.LOAD_MANAGERS)) req.setTarget(ServiceUri.Uri4Get.LOAD_MANAGERS);

        else if (parsed[1].startsWith(ServiceUri.Uri4Get.LOAD_MANAGER)) {

            req.setTarget(ServiceUri.Uri4Get.LOAD_MANAGER);

            parsed = parseOne(parsed[1].substring(ServiceUri.Uri4Get.LOAD_MANAGER.length()+1/* remove "/" */));
            if (parsed[1].equals(ServiceUri.Uri4Get.LoadManager.HTTP_OPTIONS)) {
                req.setId(parsed[0]);
                req.setWhat(ServiceUri.Uri4Get.LoadManager.HTTP_OPTIONS);
            }
        }

        return req;
    }
}
