package eu.ovmc;

import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.data.DataSource;

import java.io.*;

public class McaDataSource implements DataSource {

    private final File mcaFile;

    public McaDataSource(File mcaFile) {
        this.mcaFile = mcaFile;
    }

    @Override
    public long length() throws IOException {
        return 0;
    }

    @Override
    public byte readByte(long l) throws IOException {
        return 0;
    }

    @Override
    public void readBytes(long l, @NotNull byte[] bytes) throws IOException {

    }

    @Override
    public int readInt(long l) throws IOException {
        return 0;
    }

    @Override
    public void seek(long l) throws IOException {

    }

    @Override
    public void setLength(long l) throws IOException {

    }

    @Override
    public void writeByte(long l, byte b) throws IOException {

    }

    @Override
    public void writeBytes(long l, @NotNull byte[] bytes) throws IOException {

    }

    @Override
    public void writeInt(long l, int i) throws IOException {

    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void readFully(@NotNull byte[] b) throws IOException {

    }

    @Override
    public void readFully(@NotNull byte[] b, int off, int len) throws IOException {

    }

    @Override
    public int skipBytes(int n) throws IOException {
        return 0;
    }

    @Override
    public boolean readBoolean() throws IOException {
        return false;
    }

    @Override
    public byte readByte() throws IOException {
        return 0;
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return 0;
    }

    @Override
    public short readShort() throws IOException {
        return 0;
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return 0;
    }

    @Override
    public char readChar() throws IOException {
        return 0;
    }

    @Override
    public int readInt() throws IOException {
        return 0;
    }

    @Override
    public long readLong() throws IOException {
        return 0;
    }

    @Override
    public float readFloat() throws IOException {
        return 0;
    }

    @Override
    public double readDouble() throws IOException {
        return 0;
    }

    @Override
    public String readLine() throws IOException {
        return null;
    }

    @NotNull
    @Override
    public String readUTF() throws IOException {
        return null;
    }

    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public void write(@NotNull byte[] b) throws IOException {

    }

    @Override
    public void write(@NotNull byte[] b, int off, int len) throws IOException {

    }

    @Override
    public void writeBoolean(boolean v) throws IOException {

    }

    @Override
    public void writeByte(int v) throws IOException {

    }

    @Override
    public void writeShort(int v) throws IOException {

    }

    @Override
    public void writeChar(int v) throws IOException {

    }

    @Override
    public void writeInt(int v) throws IOException {

    }

    @Override
    public void writeLong(long v) throws IOException {

    }

    @Override
    public void writeFloat(float v) throws IOException {

    }

    @Override
    public void writeDouble(double v) throws IOException {

    }

    @Override
    public void writeBytes(@NotNull String s) throws IOException {

    }

    @Override
    public void writeChars(@NotNull String s) throws IOException {

    }

    @Override
    public void writeUTF(@NotNull String s) throws IOException {

    }
}
