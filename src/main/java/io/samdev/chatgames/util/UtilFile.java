package io.samdev.chatgames.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class UtilFile
{
    private UtilFile() {}

    public static String sha256Digest(File file)
    {
        try
        {
            return DigestUtils.sha256Hex(Files.readAllBytes(file.toPath()));
        }
        catch (IOException ex)
        {
            throw new RuntimeException("Error calculating hash of file " + file.getName());
        }
    }
}
