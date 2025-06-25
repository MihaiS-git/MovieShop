import { useUpdateAddressByIdMutation } from "@/features/addresses/addressApi";
import { Address } from "@/types/Address";
import { useState } from "react";

type Props = {
  address?: Address;
  onClose: () => void;
  refetchStore: () => void;
};

const EditStoreModal = ({ address, onClose, refetchStore }: Props) => {
  const [storeAddress, setStoreAddress] = useState(address!.address);
  const [storeAddress2, setStoreAddress2] = useState(address!.address2 || "");
  const [storeDistrict, setStoreDistrict] = useState(address!.district);
  const [storePostalCode, setStorePostalCode] = useState(address!.postalCode);
  const [storePhone, setStorePhone] = useState(address!.phone);

  const [updateAddressById, { isLoading, error }] =
    useUpdateAddressByIdMutation();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await updateAddressById({
        id: address!.id,
        data: {
          address: storeAddress,
          address2: storeAddress2,
          district: storeDistrict,
          postalCode: storePostalCode,
          phone: storePhone,
          city: address!.city.name,
          country: address!.city.country.name,
        },
      }).unwrap();

      refetchStore();
      
      onClose();
    } catch (err) {
      console.error("Failed to update store address:", err);
    }
  };

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>An error occurred during update.</p>;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white dark:bg-charcoal-800 p-6 rounded-lg w-96">
        <h2 className="text-lg font-bold mb-4">Edit Store Address</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium">Address</label>
            <input
              type="text"
              value={storeAddress}
              onChange={(e) => setStoreAddress(e.target.value)}
              className="w-full border p-2 rounded"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium">Address 2</label>
            <input
              type="text"
              value={storeAddress2}
              onChange={(e) => setStoreAddress2(e.target.value)}
              className="w-full border p-2 rounded"
            />
          </div>

          <div>
            <label className="block text-sm font-medium">District</label>
            <input
              type="text"
              value={storeDistrict}
              onChange={(e) => setStoreDistrict(e.target.value)}
              className="w-full border p-2 rounded"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium">Postal Code</label>
            <input
              type="text"
              value={storePostalCode}
              onChange={(e) => setStorePostalCode(e.target.value)}
              className="w-full border p-2 rounded"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium">Phone</label>
            <input
              type="text"
              value={storePhone}
              onChange={(e) => setStorePhone(e.target.value)}
              className="w-full border p-2 rounded"
              required
            />
          </div>

          {error && (
            <p className="text-red-500 text-sm">
              Failed to update store. Try again.
            </p>
          )}

          <div className="flex justify-end space-x-2">
            <button
              type="button"
              onClick={onClose}
              className="bg-gray-400 hover:bg-gray-500 text-white px-4 py-2 rounded"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={isLoading}
              className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded"
            >
              {isLoading ? "Saving..." : "Save"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditStoreModal;
