package org.graduate.savefile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 20:03
 * @Project : savefile
 */
@Service
public class FileStorageService implements StorageService {
    // 根路径对象
    private Path rootPath;

    private final StorageProperties properties;

    @Autowired
    public FileStorageService(StorageProperties properties) {
        this.properties = properties;
        this.rootPath = Paths.get(properties.getLocation());
    }

    @Override
    public void init(String userId) {
        try {
            this.rootPath = Paths.get(properties.getLocation() + "/" + userId);
            Files.createDirectories(rootPath);
        } catch (IOException e) {
            throw new StorageException("Could't init storage.\n");
        }
    }

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = this.rootPath.resolve(
                    Paths.get(Objects.requireNonNull(file.getOriginalFilename())))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootPath.toAbsolutePath())) {
                // This is a security check
                throw new StorageException("Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public void store(@NonNull InputStream inputStream,
                      @NonNull String fileName) {
        try {
            Path destinationFile = this.rootPath.resolve(
                    Paths.get(Objects.requireNonNull(fileName)))
                    .normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootPath.toAbsolutePath())) {
                // This is a security check
                throw new StorageException("Cannot store file outside current directory.");
            }

            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootPath, 1)
                    .filter(path -> !path.equals(this.rootPath))
                    .map(this.rootPath::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String fileName) {
        return rootPath.resolve(fileName);
    }

    @Override
    public Resource loadAsResource(String fileName) {
        try {
            Path file = load(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageException("Could not read file: " + fileName);
            }
        }
        catch (MalformedURLException e) {
            throw new StorageException("Could not read file: " + fileName, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootPath.toFile());
    }
}
