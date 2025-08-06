package me.kandid.user.Service;

import java.io.IOException;
import java.net.URL;

public interface PayUService {

    URL success(String s) throws IOException;

    URL failure(String s) throws IOException;
}
